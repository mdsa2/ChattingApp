package com.mohammed.chattingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mohammed.chattingapp.databinding.DaliogProgressBinding

open class BaseActivity : AppCompatActivity() {

    private lateinit var myprogressdialog : Dialog
    fun showErrorSnackBar(message:String,errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        if (errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorSnackBarError ))

        }else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorSnackBarSuccess))
        }
        snackBar.show()
    }
    fun showProgressDialog(text: String){
        myprogressdialog = Dialog(this)
        myprogressdialog.setContentView(R.layout.daliog_progress)

        myprogressdialog.setCancelable(false)
        myprogressdialog.setCanceledOnTouchOutside(false)
        myprogressdialog.show()


    }
    fun hideProgressDialog(){
        myprogressdialog.dismiss()


    }

}