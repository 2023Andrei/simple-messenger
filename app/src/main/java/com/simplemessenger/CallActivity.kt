package com.simplemessenger

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.webrtc.*
import java.util.*

class CallActivity : AppCompatActivity() {

    private lateinit var factory: PeerConnectionFactory
    private var peerConnection: PeerConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        // Инициализация WebRTC
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions())
        factory = PeerConnectionFactory.builder().createPeerConnectionFactory()

        createConnection()
    }

    private fun createConnection() {
        val rtcConfig = PeerConnection.RTCConfiguration(mutableListOf())
        rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN

        peerConnection = factory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
            override fun onIceConnectionReceivingChange(p0: Boolean) {}
            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
            override fun onIceCandidate(p0: IceCandidate?) {
                // Здесь нужно отправить через WebSocket на другого пользователя
                Toast.makeText(this@CallActivity, "ICE: ${p0?.sdp}", Toast.LENGTH_SHORT).show()
            }
            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
            override fun onAddStream(p0: MediaStream?) {}
            override fun onRemoveStream(p0: MediaStream?) {}
            override fun onDataChannel(p0: DataChannel?) {}
            override fun onRenegotiationNeeded() {}
            override fun onConnectionChange(p0: PeerConnection.PeerConnectionState?) {}
        })

        // Создаём аудио-трек
        val audioSource = factory.createAudioSource(AudioConstraints())
        val audioTrack = factory.createAudioTrack("audio1", audioSource)
        peerConnection?.addTrack(audioTrack)
    }

    private fun AudioConstraints(): MediaConstraints {
        val constraints = MediaConstraints()
        constraints.mandatory.add(MediaConstraints.KeyValuePair("echoCancellation", "true"))
        constraints.mandatory.add(MediaConstraints.KeyValuePair("noiseSuppression", "true"))
        return constraints
    }
}
