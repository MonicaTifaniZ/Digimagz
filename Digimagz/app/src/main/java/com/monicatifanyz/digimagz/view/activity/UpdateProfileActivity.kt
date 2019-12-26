package com.monicatifanyz.digimagz.view.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.alert_edit_gender.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var initRetrofit:InitRetrofit

    private lateinit var date:String
    private lateinit var gender:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

       initRetrofit = InitRetrofit()

        gender = "L"

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }


        radioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.radioButtonMan){
                    gender = "L"
                } else if (checkedId == R.id.radioButtonMan){
                    gender = "P"
                }
            }
        })

        materialButtonUpdate.setOnClickListener {
            initRetrofit.putUserToApi(firebaseUser?.email.toString() , firebaseUser?.displayName.toString(), firebaseUser?.photoUrl.toString(), date, gender)
            val intent:Intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
    fun showDatePickerDialog(){
        var newCalendar:Calendar = Calendar.getInstance()
        var datePickerDialog:DatePickerDialog = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view , year, monthOfYear, dayOfMonth ->
            var newDate : Calendar = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)
            var dateFormatter:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("in","ID"))
            var dateFormatterText:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("in","ID"))

            editTextDate.setText(dateFormatterText.format(newDate.time))
            date = dateFormatter.format(newDate.time)


        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }
}
