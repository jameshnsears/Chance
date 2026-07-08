package com.github.jameshnsears.chance.ui.tab.rolls

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import timber.log.Timber
import kotlin.math.sqrt

class ShakeToRollService(
    context: Context,
    private val onShake: () -> Unit
) : SensorEventListener {
    private val sensorManager = try {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    } catch (e: Exception) {
        null
    }
    private val accelerometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var shakeTimestamp: Long = 0
    private val shakeThresholdGravity = 2.7f
    private val shakeSlopTimeMs = 500
    private var shakeStartTime: Long = 0
    private val shakeDurationThresholdMs = 250
    private var lastHighGTime: Long = 0
    private val shakeGraceTimeMs = 150

    private var isStarted = false

    fun start() {
        if (!isStarted) {
            Timber.d("start")
            accelerometer?.let {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
                isStarted = true
            }
        }
    }

    fun stop() {
        if (isStarted) {
            Timber.d("stop")
            sensorManager?.unregisterListener(this)
            isStarted = false
            shakeStartTime = 0
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // gForce will be close to 1 when there is no movement.
            val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

            val now = System.currentTimeMillis()
            if (gForce > shakeThresholdGravity) {
                if (shakeStartTime == 0L) {
                    shakeStartTime = now
                }
                lastHighGTime = now

                if (now - shakeStartTime >= shakeDurationThresholdMs) {
                    // ignore shake events too close to each other
                    if (now - shakeTimestamp > shakeSlopTimeMs) {
                        shakeTimestamp = now
                        Timber.d("onShake")
                        onShake()
                        shakeStartTime = 0
                    }
                }
            } else {
                if (now - lastHighGTime > shakeGraceTimeMs) {
                    shakeStartTime = 0
                }
            }
        }
    }
}
