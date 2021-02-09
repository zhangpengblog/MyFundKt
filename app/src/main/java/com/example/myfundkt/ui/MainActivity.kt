package com.example.myfundkt.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myfundkt.R
import com.example.myfundkt.db.DbRepository
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.utils.ToastUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        fab.setOnClickListener {
            showAddDialog()
        }

        Log.d(TAG, "onCreate: $title")




    }


    fun showAddDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        val builder = AlertDialog.Builder(this@MainActivity)
        val dialogView: View = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.dialog_add, null)
        builder.setView(dialogView)
        val dialog = builder.show()
        val button = dialogView.findViewById<Button>(R.id.button)
        val editText1: TextInputEditText = dialogView.findViewById(R.id.code)
        val editText2: TextInputEditText = dialogView.findViewById(R.id.cost)
        val editText3: TextInputEditText = dialogView.findViewById(R.id.quantity)
        button.setOnClickListener { view: View? ->
            if (!title.equals("首页")){
                ToastUtil.show("请先返回首页添加")
                return@setOnClickListener
            }
            val s1 = editText1.text.toString()
            val s2 = editText2.text.toString()
            val s3 = editText3.text.toString()
            if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
                Snackbar.make(view!!, "不能为空", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            } else {
                val cost = s2.toDouble()
                val quantity = s3.toDouble()
                val foudInfoEntity = FoudInfoEntity(s1, quantity, cost)
                val repository = DbRepository()
                var amount = 0
                val codes: List<String> = repository.GetCodes()
                if (codes.isNotEmpty()) {
                    amount = codes.size
                }
                repository.InsertInfo(foudInfoEntity)
//              KtRepositiry().insert(foudInfoEntity)

//                GlobalScope.launch {
//                    var dbMaster: DbMaster? = DbMaster.getDbMaster()
//                    var foudInfoDao: FoudInfoDao? = dbMaster?.foudInfoDao
//                    foudInfoDao?.insertFoudInfo(foudInfoEntity)
//                }.isCompleted

                if (amount == repository.GetCodes().size) {
                    Snackbar.make(view!!, "添加失败", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    Snackbar.make(view!!, "添加成功", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    navController.navigate(R.id.myFundFragment)
                    dialog.dismiss()
                }
            }
        }

        val importData = dialogView.findViewById<Button>(R.id.importFrom)
        importData.setOnClickListener {
            if (title.equals("首页")){
                dialog.dismiss()
                navController.navigate(R.id.action_myFundFragment_to_importDataFragment)
            }else{
                ToastUtil.show("请先返回首页导入")
            }

        }
    }


}