package com.example.hw1

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.lang.ref.WeakReference
import kotlin.math.pow
import kotlin.math.sqrt

class GyroscopeListener(context: Context) : SensorEventListener {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val intent = Intent(context, MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    private var overallAngularVelocity = 0.0

    init {
        if (gyroscopeSensor == null) {
            Log.e("GyroscopeListener", "Gyroscope sensor not found")
        }
    }

    fun startListening() {
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing for now
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onSensorChanged(event: SensorEvent) {
        val context = contextRef.get()
        val angularVelocityX = event.values[0]
        val angularVelocityY = event.values[1]
        val angularVelocityZ = event.values[2]

        // Calculate the overall angular velocity
        overallAngularVelocity = sqrt(
            angularVelocityX.toDouble().pow(2.0) +
               angularVelocityY.toDouble().pow(2.0) +
               angularVelocityZ.toDouble().pow(2.0)
        )

        // Set a threshold for spinning detection
        val spinningThreshold = 2.0

        // Check if the overall angular velocity exceeds the threshold
        val isSpinning = overallAngularVelocity > spinningThreshold

        if (isSpinning) {
            if (context != null) {
                showNotification(context)
            }
            //stopListening() // Optionally stop listening after detecting spinning
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotification(context: Context) {
        val channelId = "Gyroscope Notifications"
        createNotificationChannel(context, channelId)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Spinning Detected")
            .setContentText("You are spinning at %.3f!".format(overallAngularVelocity.toDouble()))            .setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setContentIntent(pendingIntent).setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as MainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    124
                )
            }
            notify(123, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Gyroscope Notifications"
            val descriptionText = "Your Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH // Set importance to high
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
