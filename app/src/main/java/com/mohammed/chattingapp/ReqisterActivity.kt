package com.mohammed.chattingapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.mohammed.chattingapp.databinding.ActivityReqisterBinding


open class ReqisterActivity : BaseActivity() {
    private lateinit var binding: ActivityReqisterBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReqisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.dwaw.setOnClickListener {
            registerUser(it.toString())



        }

    }

    private fun validateRegisterDetalis(): Boolean {
        return when {
            TextUtils.isEmpty(binding.nameReg.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(binding.Email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false

            }
            TextUtils.isEmpty(binding.password1.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false


            }
            TextUtils.isEmpty(binding.password2.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false


            }
            binding.password1.text.toString()
                .trim { it <= ' ' } != binding.password2.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false


            }


            binding.aq.isChecked -> {

                showErrorSnackBar(resources.getString(R.string.registery_succefull), false)
                true

            }
            else -> {

                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condation),
                    true
                )
                false


            }


        }


    }

    private fun registerUser(imageuri: String) {

        if (validateRegisterDetalis()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val email = binding.Email.text.toString().trim { it <= ' ' }
            val password = binding.password1.text.toString().trim { it <= ' ' }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        hideProgressDialog()
                        if (task.isSuccessful) {

                            val firebases: FirebaseUser = task.result!!.user!!
 //this is save to user databse
                            var auth: FirebaseAuth = FirebaseAuth.getInstance()
                            val firebaseUser = auth.currentUser
                            assert(firebaseUser != null)
                            val uid = firebaseUser?.uid.toString()
                            val username = binding.nameReg.text.toString()
                            val email = binding.Email.text.toString()

                            val ref = FirebaseDatabase.getInstance().getReference("Users").child(uid)


                            val hasMap: HashMap<String, String> = hashMapOf()
                            hasMap.put("uid", uid)
                            hasMap.put("username", username)

                            hasMap.put("emailedt", email)
                            hasMap.put("imageuri", "")


                            //input database
                            ref.setValue(hasMap).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(this, NewMessage::class.java)
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    it.exception.toString()
                                    val failedMsg = it.exception?.message

                                    Toast.makeText(this, failedMsg, Toast.LENGTH_LONG).show()

                                }

                            }
                                showErrorSnackBar(

                                "you are registerd successfully.your user id is ${firebases.uid}",
                                false

                            )

                            val intent = Intent(this, NewMessage::class.java)
                            startActivity(intent)

                            finish()


                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })


        }

    }




}
