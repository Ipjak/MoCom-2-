package NB.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import NB.data.REF
import NB.data.MyUser
import NB.util.SignInIntentContract

class LoginActivity : AppCompatActivity() {
    val PASSED = "익명"

    private var launcher: ActivityResultLauncher<String>? = null
    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        launcher = registerForActivityResult(SignInIntentContract()) { result: String? ->
            result?.let {
                firebaseAuthWithGoogle(it)  //tokenId를 이용해 firebase에 인증하는 함수 호출.
            }
        }
        // 구글  로그인 버튼
        findViewById<Button>(R.id.main_google_button).setOnClickListener {
            //Launcher를 실행해 LoginActivity -> 구글 로그인 화면으로 이동.
            launcher!!.launch(getString(R.string.default_web_client_id))
        //왜 빨강이지
        }

        // 카카오 로그인 버튼
        findViewById<Button>(R.id.main_kakao_button).setOnClickListener {
            Toast.makeText(this, "카카오 로그인", Toast.LENGTH_LONG).show()
        }

        // 익명 버튼
        findViewById<Button>(R.id.main_pass_button).setOnClickListener {
            auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success")
                            val user = auth.currentUser
                            var intent = Intent(this, BoardActivity::class.java)
                            intent.putExtra(REF.USER.name,
                                MyUser(PASSED, PASSED)
                            )
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }

        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val name = user?.displayName
                        val mail = user?.email

                        var intent = Intent(this, BoardActivity::class.java)
                        intent.putExtra(REF.USER.name,
                            MyUser(name, mail)
                        )
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@LoginActivity, R.string.signin_fail, Toast.LENGTH_LONG).show()
                    }
                }
    }
}