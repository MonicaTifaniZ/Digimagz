package com.monicatifanyz.digimagz.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.Constant
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.ApiClient
import com.monicatifanyz.digimagz.api.ApiInterface
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.AvatarModel
import com.monicatifanyz.digimagz.model.UserModel
import com.monicatifanyz.digimagz.view.activity.MainActivity
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileFragment : Fragment() {

    private lateinit var initRetrofit: InitRetrofit
    private lateinit var apiInterface: ApiInterface
    private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val REQUEST_CAMERA = 1
    private val SELECT_FILE = 2

    private lateinit var profileFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        initRetrofit = InitRetrofit()
        apiInterface = ApiClient().getRetrofit()!!.create(ApiInterface::class.java)

        initRetrofit.getUserFromApi(firebaseUser?.email.toString())
        initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList!!.isNotEmpty()) {
                    var arrayListUser: ArrayList<UserModel> = ArrayList<UserModel>()
                    arrayListUser = arrayList.clone() as ArrayList<UserModel>

                    Glide.with(activity!!)
                        .load(arrayListUser[0].urlPic)
                        .placeholder(R.color.chef)
                        .into(imgProfile)

                    tvNameProfile.text = arrayListUser[0].userName
                    if(arrayListUser[0].gender.equals("L")) {
                        tvGender.text = "Laki-laki"
                    } else {
                        tvGender.text = "Perempuan"
                    }

                    val format = SimpleDateFormat("yyyy-MM-dd", Locale("in", "ID"))
                    val date = format.parse(arrayListUser[0].dateBirth)
                    val dateFormatterText = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))

                    tvAge.text = dateFormatterText.format(date.time)
                    tvEmailProfile.text = arrayListUser[0].email
                    tvStatus.text = arrayListUser[0].userType
                }
            }

        })

        view.materialButtonSignOut.setOnClickListener {
            signOut()
        }

        view.btnCamera.setOnClickListener {
            selectImage()
        }

        return view
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            "Ambil Foto",
            "Ambil dari Galeri",
            "Batal"
        )
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Foto Profil")
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Ambil Foto") {
                try {
                    profileFile = File(Objects.requireNonNull(Objects.requireNonNull(activity)?.externalCacheDir).toString() + "/Digimagz" + System.currentTimeMillis())
                    val uri = FileProvider.getUriForFile(activity!!, activity!!.applicationContext.packageName + ".provider", profileFile)
                    //use standard intent to capture an image
                    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    captureIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(captureIntent, ProfileFragment().REQUEST_CAMERA)
                } catch (anfe: ActivityNotFoundException) { //display an error message
                    val errorMessage = "Whoops - your device doesn't support capturing images!"
                    val toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                    toast.show()
                }
            } else if (items[item] == "Ambil dari Galeri") {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    ProfileFragment().SELECT_FILE
                )
            } else if (items[item] == "Batal") {
                dialog.dismiss()
            } else {
                dialog.dismiss()
            }
        }

        builder.show()
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source!!, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun saveBitmapToFile(
        uri: Uri,
        fromCamera: Boolean
    ): String? {
        return try { // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var imageStream = Objects.requireNonNull(activity)?.contentResolver?.openInputStream(uri)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(imageStream, null, o)
            assert(imageStream != null)
            imageStream!!.close()
            // The new size we want to scale to
            val REQUIRED_SIZE = 70
            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            imageStream = activity!!.contentResolver.openInputStream(uri)
            val selectedBitmap = BitmapFactory.decodeStream(imageStream, null, o2)
            //Bitmap rotatedBMP = rotateImage(selectedBitmap, 90);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val projection2 = arrayOf(
                    MediaStore.Images.ImageColumns.ORIENTATION
                )
            }
            val orientation: Int = Constant().getOrientationFromURI(context!!, Uri.parse(uri.path))
            val rotatedBMP = rotateImage(selectedBitmap, orientation.toFloat())
            assert(imageStream != null)
            imageStream!!.close()
            //FileOutputStream outputStream = new FileOutputStream(file);
            val outputStream = ByteArrayOutputStream()
            rotatedBMP.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            //imgProfile.setImageBitmap(rotatedBMP);
            val byteGambar = outputStream.toByteArray()
            if (fromCamera) {
                val projection = arrayOf(
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
                )
                val cursor2 = activity!!.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")
                var imageLocation: String? = ""
                if (cursor2!!.moveToFirst()) {
                    imageLocation = cursor2.getString(1)
                    val id = cursor2.getLong(cursor2.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    activity!!.contentResolver.delete(deleteUri, null, null)
                }
                cursor2.close()
            }
            Base64.encodeToString(
                byteGambar,
                Base64.NO_WRAP
            )
            //return file;
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .start(context!!, this)
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(context!!)
            .addOnCompleteListener {
                toMain()
            }
            .addOnFailureListener {
                Log.e("ErrorSignOut", it.message.toString())
            }
    }

    private fun toMain() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ProfileFragment().SELECT_FILE) {
                val imageUri = CropImage.getPickImageResultUri(
                    Objects.requireNonNull(context!!), data
                )
                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(
                        context!!,
                        imageUri
                    )
                ) { // request permissions and handle the result in onRequestPermissionsResult()
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        0
                    )
                } else { // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri)
                }
            } else if (requestCode == ProfileFragment().REQUEST_CAMERA) {
                val uri = FileProvider.getUriForFile(
                    Objects.requireNonNull(activity!!),
                    activity!!.applicationContext
                        .packageName + ".provider",
                    profileFile
                )
                startCropImageActivity(uri)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    var compFile: File? = null
                    try {
                        compFile =
                            Compressor(Objects.requireNonNull(context)).compressToFile(
                                File(Objects.requireNonNull(result.uri.path))
                            )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val comUri = Uri.fromFile(compFile)
                    val base64 = saveBitmapToFile(comUri, if (requestCode == ProfileFragment().REQUEST_CAMERA) true else false)
                    progressBar.visibility = View.VISIBLE
                    //RequestBody picture = RequestBody.create(MediaType.parse("image/*"), compFile.getName());
                    val filePart: MultipartBody.Part = createFormData("picture", compFile!!.name, RequestBody.create("image/*".toMediaTypeOrNull(), compFile))
                    val email: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), tvEmailProfile.text.toString())
                    val finalCompFile = compFile
                    apiInterface.postAvatar(email, filePart)
                        .enqueue(object : Callback<AvatarModel> {
                            override fun onResponse(
                                call: Call<AvatarModel>,
                                response: Response<AvatarModel>
                            ) {
                                Log.e("Avatar", "Sukses")
                                progressBar.visibility = View.GONE
                                Glide.with(activity!!)
                                    .asBitmap()
                                    .load(response.body()!!.message)
                                    .placeholder(R.color.chef)
                                    .into(imgProfile)
                            }

                            override fun onFailure(
                                call: Call<AvatarModel>,
                                t: Throwable
                            ) {
                                Log.e("Avatar", t.message)
                            }
                        })
                }
            }
        }
    }
}

