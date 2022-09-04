package com.mohammed.chattingapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.mohammed.chattingapp.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var auth:FirebaseAuth
    lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        //auth= FirebaseAuth.getInstance()
    /*  var firebaseUser: FirebaseUser?=auth.currentUser
        if(firebaseUser !=null) {
            val intent = Intent(this@LoginActivity, NewMessage::class.java)
            startActivity(intent)
            finish()
        }*/
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding.idButtonlog.setOnClickListener(this)
        binding.idcreate.setOnClickListener(this)
        binding.ForgotPass.setOnClickListener(this)

binding.fasa.setOnClickListener {
    startActivity(Intent(this,ForgotPassword::class.java))
}
    }
    override fun onClick(view : View?){
        if (view !=null){
            when(view.id){

                R.id.idButtonlog -> {
                    loginRegisterUser()

                }
                R.id.idcreate -> {
                    startActivity(Intent(this,ReqisterActivity::class.java))

                }



            }


        }



    }
    private fun validateLoginDetails():Boolean{
        return when {
            TextUtils.isEmpty(binding.idUser.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }

            TextUtils.isEmpty(binding.idPassword.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {

                true


            }


        }
    }
    private fun loginRegisterUser() {
        if (validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val email = binding.idUser.text.toString().trim{it <= ' '}
            val password = binding.idPassword.text.toString().trim {it <= ' '}
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->

                    hideProgressDialog()
                    if (task.isSuccessful) {
                        showErrorSnackBar("you are logged in successfully", false)
                        startActivity(Intent(this,NewMessage::class.java))
                        finish()

                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)

                    }
                }

        }

    }




}