package com.ttlabs.bt_spike2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


const val RC_SIGN_IN = 12

class MainActivity : AppCompatActivity() {

    private var TAG ="MainActivity"
    private lateinit var signInButton:SignInButton
    private lateinit var text:TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signInButton = findViewById(R.id.sign_in_button)
        text = findViewById(R.id.text)
        signOutButton = findViewById(R.id.sign_in_out)

        /** Configure sign-in to request the user's ID, email address, and basic profile.
        ID and basic profile are included in DEFAULT_SIGN_IN. **/
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        /** Build a GoogleSignInClient with the options specified by gso. */
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         /**Check for existing Google Sign In account, if the user is already signed
        in the GoogleSignInAccount will be non-null.**/
        val account = GoogleSignIn.getLastSignedInAccount(this)

        signInButton.visibility = View.VISIBLE
        signOutButton.visibility = View.GONE
        text.visibility = View.GONE
        signInButton.setSize(SignInButton.SIZE_WIDE)

       signInButton.setOnClickListener {
           val signInIntent = mGoogleSignInClient.signInIntent
           startActivityForResult(signInIntent, RC_SIGN_IN)
       }

        signOutButton.setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this) {
                    signInButton.visibility = View.VISIBLE
                    signOutButton.visibility = View.GONE
                    text.visibility = View.GONE
                }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
            signInButton.visibility = View.GONE
            text.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            text.text = account.displayName


        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            signInButton.visibility = View.VISIBLE
            signOutButton.visibility = View.GONE
            text.visibility = View.GONE
//            updateUI(null)
        }
    }

}