package com.simplemessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editPhone: EditText
    private lateinit var editCode: EditText
    private lateinit var btnSendCode: Button
    private lateinit var btnVerifyCode: Button

    private var storedVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        editPhone = findViewById(R.id.editPhone)
        editCode = findViewById(R.id.editCode)
        btnSendCode = findViewById(R.id.btnSendCode)
        btnVerifyCode = findViewById(R.id.btnVerifyCode)

        btnSendCode.setOnClickListener {
            val phone = editPhone.text.toString().trim()
            if (phone.isNotEmpty() && phone.startsWith("+")) {
                sendVerificationCode(phone)
            } else {
                Toast.makeText(this, "Введите номер в формате +79991234567", Toast.LENGTH_SHORT).show()
            }
        }

        btnVerifyCode.setOnClickListener {
            val code = editCode.text.toString().trim()
            if (code.isNotEmpty() && storedVerificationId != null) {
                verifyCode(code)
            }
        }
    }

    private fun sendVerificationCode(phone: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signIn(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@LoginActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    storedVerificationId = verificationId
                    Toast.makeText(this@LoginActivity, "Код отправлен", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signIn(credential)
    }

    private fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, ChatActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Вход не удался", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
