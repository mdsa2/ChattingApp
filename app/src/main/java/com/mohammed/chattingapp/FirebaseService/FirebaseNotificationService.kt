package com.mohammed.chattingapp.FirebaseService

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Resources
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mohammed.chattingapp.Appconstant.AppConstants
import com.mohammed.chattingapp.R
import com.mohammed.chattingapp.Util.AppUtil
import com.mohammed.chattingapp.chatActivity
import java.util.*
import kotlin.collections.HashMap


class FirebaseNotificationService: FirebaseMessagingService() {
private val appUtil=AppUtil()
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updataToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage ) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty())
        {
            val map:Map<String,String> = remoteMessage.data
            val title=map["title"]
            val message=map ["message"]
            val hisId=map["hisId"]
            val hisImage=map["hisImage"]
            val chatId=map["chatId"]
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                createOreoNotification(title!!,message!!,hisId!!,hisImage!!,chatId!!)

            }else createNormalNotification(title!!,message!!,hisId!!,hisImage!!,chatId!!)
        }

    }
    private fun updataToken (token:String){
        val databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(appUtil.getUID()!!)
        val map:MutableMap<String,Any> = HashMap()
        map["token"]=token
        databaseReference.updateChildren(map)

    }
    private fun createNormalNotification(

        title:String,
        message:String,
        hisId:String,
        hisImage:String,
        chatId:String
    ){
        val uri =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder =NotificationCompat.Builder(this,AppConstants.CHANNEL_ID)
builder.setContentTitle(title)
    .setContentText(message)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setSmallIcon(R.drawable.myimage)
    .setAutoCancel(true)
    .setColor(ResourcesCompat.getColor(resources,R.color.colorPrimary,null))
    .setSound(uri)
        val intent =Intent(this,chatActivity.javaClass)
        intent.putExtra("hisId",hisId)
        intent.putExtra("hisImage",hisImage)
        intent.putExtra("chatId",chatId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        builder.setContentIntent(pendingIntent)
        val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Random().nextInt(85-65),builder.build())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOreoNotification(
        title:String,
        message:String,
        hisId:String,
        hisImage:String,
        chatId:String
    )
    {
        val chanel = NotificationChannel(
            AppConstants.CHANNEL_ID,
            "Message",NotificationManager.IMPORTANCE_HIGH
        )
        chanel.setShowBadge(true)
        chanel.enableLights(true)
        chanel.enableVibration(true)
        chanel.lockscreenVisibility= Notification.VISIBILITY_PRIVATE
val manager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chanel)
        val intent =Intent(this,chatActivity.javaClass)
        intent.putExtra("hisId",hisId)
        intent.putExtra("hisImage",hisImage)
        intent.putExtra("chatId",chatId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val notification =Notification.Builder(this,AppConstants.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.myimage)
            .setAutoCancel(true)
            .setColor(ResourcesCompat.getColor(resources,R.color.colorPrimary,null))
            .setContentIntent(pendingIntent)
            .build()
        manager.notify(100,notification)
    }
}