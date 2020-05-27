package com.jwoo.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        // sendDataToFirebase("JWOO")
        // sendDataToFirebasev2("HELLO")
        // readFromFirebase()
        // signInUser()
        // readFromFirebase()

        btnSave.setOnClickListener {
            var userName = txtEmail.text.toString()
            var passWord = txtPassword.text.toString()
            createNewUser(userName, passWord)
        }
    }

    fun sendDataToFirebase(str: String){
        var firebaseDb:FirebaseDatabase = FirebaseDatabase.getInstance()
        var databaseRef: DatabaseReference = firebaseDb.reference

        databaseRef.setValue(str)
    }

    fun sendDataToFirebasev2(str: String){
        var firebaseDb:FirebaseDatabase = FirebaseDatabase.getInstance()
        var databaseRef: DatabaseReference = firebaseDb.getReference("messagesV2").push()

        var tstClass = TestClass("JWOO", str)

        databaseRef.setValue(tstClass)
    }

    fun readFromFirebase(){
        var firebaseDb:FirebaseDatabase = FirebaseDatabase.getInstance()
        var databaseRef: DatabaseReference = firebaseDb.getReference("messagesV2")
        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var value = dataSnapshot!!.value
                var valHashMap = value as HashMap<String, Any>

                Log.d("FirebaseSuccess", value.toString())
                Log.d("FirebaseSuccess", valHashMap.get("msg").toString())
                Log.d("FirebaseSuccess", valHashMap.get("name").toString())
            }

            override fun onCancelled(error: DatabaseError) {

                Log.d("FirebaseError", error.toString())
            }
        })
    }

    fun signInUser(){

        mAuth!!.signInWithEmailAndPassword("johntotetwoo@yahoo.com","!596688Fi8118809!")
                .addOnCompleteListener{
                    task: Task<AuthResult> ->
                    if (task.isSuccessful){
                        var user:FirebaseUser = mAuth!!.currentUser!!
                        Log.d("FirebaseSignIn", "SUCCESS - "  + user.email.toString())
                    }
                    else {
                        Log.d("FirebaseSignIn", "FAILED")
                    }
                }
    }

    fun logOutUser(){

    }

    fun createNewUser(userName:String, passWord:String){
        mAuth!!.createUserWithEmailAndPassword(userName, passWord)
                .addOnCompleteListener(this, {
                    task: Task<AuthResult> ->
                        if (task.isSuccessful){
                            var user:FirebaseUser = mAuth!!.currentUser!!
                            Log.d("FirebaseCreateUser", "SUCCESS - "  + user.email.toString())
                        }
                        else {
                            Log.d("FirebaseCreateUser", "FAILED")
                        }
                })
    }

    override fun onStart() {
        super.onStart()

        currentUser = mAuth!!.currentUser
        if (currentUser != null){
            Log.d("FirebaseCurrentUser", "User is logged in. - " + currentUser!!.email.toString())
        }
        else{
            Log.d("FirebaseCurrentUser", "User is not logged in.")
        }

    }

    data class TestClass(val name: String, val msg: String)
}


