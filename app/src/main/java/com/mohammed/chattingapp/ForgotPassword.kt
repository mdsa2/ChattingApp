package com.mohammed.chattingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mohammed.chattingapp.databinding.ActivityForgotPasswordBinding

class ForgotPassword : BaseActivity() {
    lateinit var binding:ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView( view)

        binding.sendbtn.setOnClickListener {
            val email: String = binding.emialtext.text.toString().trim {it <= ' '}
            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)

            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task ->
                        hideProgressDialog()
                        if (task.isSuccessful){
                            Toast.makeText(this,resources.getString(R.string.email_sent_success), Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }


                    }
            }

        }

    }



}