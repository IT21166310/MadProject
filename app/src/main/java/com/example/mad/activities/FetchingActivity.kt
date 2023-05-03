package com.example.mad.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad.R
import com.example.mad.adapters.PlanAdapter
import com.example.mad.models.PlanModel
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FetchingActivity:AppCompatActivity() {

    private lateinit var incomeRecyclerView: RecyclerView
    private lateinit var tvloadingData: TextView
    private lateinit var incomeList: ArrayList<PlanModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        incomeRecyclerView=findViewById(R.id.rvPlan)
        incomeRecyclerView.layoutManager=LinearLayoutManager(this)
        incomeRecyclerView.setHasFixedSize(true)
        tvloadingData = findViewById(R.id.tvLoadingData)

        incomeList= arrayListOf<PlanModel>()

        getPlanData()
    }
    private fun getPlanData(){
        incomeRecyclerView.visibility=View.GONE
        tvloadingData.visibility=View.VISIBLE

        val dbRef = FirebaseDatabase.getInstance().getReference("income")

        dbRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                incomeList.clear()
                if (snapshot.exists()){
                    for(planSnap in snapshot.children){
                        val planData = planSnap.getValue(PlanModel::class.java)
                        incomeList.add(planData!!)
                    }
                    val pAdapter = PlanAdapter(incomeList)
                    incomeRecyclerView.adapter=pAdapter

                    pAdapter.setonItemClickListener(object :PlanAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            //user directed to see more data
                            val intent = Intent(this@FetchingActivity, planDetailsActivity::class.java)

                            //put extra view
                            intent.putExtra("planId",incomeList[position].planId)
                            intent.putExtra("planName",incomeList[position].planName)
                            intent.putExtra("income",incomeList[position].income)
                            startActivity(intent)

                        }

                    })

                    incomeRecyclerView.visibility= View.VISIBLE
                    tvloadingData.visibility=View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}