package com.example.mad.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad.models.PlanModel
import com.example.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity:AppCompatActivity() {

    private lateinit var editname: EditText
    private lateinit var incomeamnt:EditText
    private lateinit var btnCreate: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        editname = findViewById(R.id.editname)
        incomeamnt = findViewById(R.id.incomeamnt)
        btnCreate = findViewById(R.id.btnCreate)

        dbRef= FirebaseDatabase.getInstance().getReference("income")

        btnCreate.setOnClickListener {
            createBudgetplan ()
        }
    }

    private fun createBudgetplan() {
        //getting values
        val incometype =editname.text.toString()
        val amount = incomeamnt.text.toString()

        if (incometype.isEmpty()){
            editname.error = "Please enter the plan Name"
        }
        if (amount.isEmpty()){
            incomeamnt.error = "Please enter the Income"
        }

        val income = dbRef.push().key!!

        val plan = PlanModel (income, incometype, amount)

        dbRef.child(income).setValue(plan)

            .addOnCompleteListener {
                Toast.makeText(this,"Income Added!", Toast.LENGTH_LONG).show()

                editname.text.clear()
                incomeamnt.text.clear()


            }.addOnFailureListener{err->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }
}



