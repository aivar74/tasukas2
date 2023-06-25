package com.example.tasukas2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var isLampOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.centerButton)
        button.setOnClickListener {
            toggleLamp()
        }

        // Halduri ja kaamera ID initsialiseerimine
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraId = getCameraId()
    }

    private fun toggleLamp() {
        if (isLampOn) {
            // Kui lamp on sisse lülitatud, lülita välja
            turnOffLamp()
        } else {
            // Kui lamp on välja lülitatud, lülita sisse
            turnOnLamp()
        }
    }

    private fun turnOnLamp() {
        try {
            cameraManager.setTorchMode(cameraId, true) // Lülita sisse lamp
            isLampOn = true
            setButtonColor(isLampOn) // Värvi nupp roheliseks
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun turnOffLamp() {
        try {
            cameraManager.setTorchMode(cameraId, false) // Lülita välja lamp
            isLampOn = false
            setButtonColor(isLampOn) // Värvi nupp punaseks
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun getCameraId(): String {
        val cameraIds = cameraManager.cameraIdList
        for (id in cameraIds) {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (flashAvailable != null && flashAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }
        }
        throw IllegalStateException("Camera with flash not found.")
    }

    private fun setButtonColor(isLampOn: Boolean) {
        val colorRes = if (isLampOn) R.color.green else R.color.red
        val color = ContextCompat.getColor(this, colorRes)
        button.setBackgroundColor(color)
    }
}
