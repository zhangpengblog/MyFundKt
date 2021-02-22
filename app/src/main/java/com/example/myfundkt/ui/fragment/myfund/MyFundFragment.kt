package com.example.myfundkt.ui.fragment.myfund

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.example.myfundkt.R
import com.example.myfundkt.adapter.SelectionAdapter
import com.example.myfundkt.adapter.TopAdapter
import com.example.myfundkt.bean.CollectionBean
import com.example.myfundkt.bean.top.Diff
import com.example.myfundkt.databinding.FragmentMyFundBinding
import com.example.myfundkt.db.DbRepository
import com.example.myfundkt.db.KtDatabase
import com.example.myfundkt.db.dao.FoudInfoDao
import com.example.myfundkt.db.entity.FoudInfoEntity
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.utils.MyLog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*


private const val TAG = "MyFundFragment"
private val topData = mutableListOf<Diff>()

private val selectionData = mutableListOf<CollectionBean>()



class MyFundFragment : Fragment() {
    private lateinit var myViewModel: MyViewModel
    private  lateinit var binding: FragmentMyFundBinding
    private var topAdapter: TopAdapter = TopAdapter(topData)
    private var selectionAdapter: SelectionAdapter = SelectionAdapter(selectionData)
    private lateinit var controller: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        activity?.title = "首页"

        binding = FragmentMyFundBinding.inflate(inflater,container,false)
//        return inflater.inflate(R.layout.fragment_my_fund, container, false)
        return binding.root
    }

    
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = view.let { Navigation.findNavController(it) }
        binding.fab.setOnClickListener {
            showAddDialog()
        }
        //滑动的时候隐藏or显示fab
        binding.nestedscrollview.setOnScrollChangeListener(object :View.OnScrollChangeListener{
            override fun onScrollChange(
                v: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {

                if (scrollY-oldScrollY>30){
                    if (binding.fab.visibility == View.VISIBLE){
                        val hideAnim = TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 1.0f
                        )
                        hideAnim.duration = 300
                        binding.fab.startAnimation(hideAnim)
                    }

                    binding.fab.visibility=View.GONE
                }
                if (oldScrollY-scrollY>30){
                    if (binding.fab.visibility == View.GONE){
                        val showAnim = TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f
                        )
                        showAnim.duration = 300
                        binding.fab.startAnimation(showAnim)
                    }

                    binding.fab.visibility=View.VISIBLE
                }
            }

        })

        binding.refreshLayout.setOnRefreshListener {
            refreshData()
        }

        binding.top.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.top.adapter = topAdapter

        binding.bottom.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.bottom.adapter= selectionAdapter
        selectionAdapter.setOnItemClickListener { _, _, position ->

            val args = Bundle()
            args.putString("代码", selectionData[position].代码)
//                controller.navigate(R.id.action_myFundFragment_to_fundInfoFragment,args)
            myViewModel.infomationCode.value = selectionData[position].代码
            MyLog.d(TAG,"代码"+selectionData[position].代码)

            controller.navigate(R.id.action_myFundFragment_to_pagesFragment)


        }
        selectionAdapter.setOnItemLongClickListener(object : OnItemLongClickListener {

            override fun onItemLongClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ): Boolean {

                delete(position)
                return true
            }

        })


    }

    private fun delete(position:Int){
        val fund = selectionData[position]
        val code = fund.代码
        lifecycleScope.launch{
            val ktDao=KtDatabase.dataBase.getDao()
            val entity=ktDao.FindByCode(code?:"0")
            val id = entity?.id
            activity?.let {
                AlertDialog.Builder(it).setMessage("删除").setPositiveButton("删除",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        lifecycleScope.launch {
                            if (id != null) {
                                ktDao.deleteById(id)
                                selectionAdapter.removeAt(position)
                                selectionAdapter.notifyDataSetChanged()
                            }
                        }

                    })
            }?.setNegativeButton("取消", null)
                ?.create()?.show()

        }

    }

    private fun refreshData() {
        myViewModel.let {
            it.initCode()
//            it.initIndexFund()
            it.initFundCoro()
//            it.initSellectionFund()
            it.initSelectedFundCoro()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java).apply {

//            initIndexFund()
//            initFundCoro()
//            initSelectedFundCoro()
//            getHoliday()

            progressBarVisibility.observe(viewLifecycleOwner, {
                binding.progressBar.visibility = it
            })

            topLiveData.observe(viewLifecycleOwner, {
                binding.refreshLayout.finishRefresh()
                Log.d(TAG, "onActivityCreated:topLiveData "+it)
                topData.clear()
                topData.addAll(it)
                topAdapter.notifyDataSetChanged()

            })
            collection.observe(viewLifecycleOwner, {
                binding.refreshLayout.finishRefresh()
                if (it != null) {
                    selectionData.clear()
                    selectionData.addAll(it)
                    selectionAdapter.notifyDataSetChanged()
                    var amount = 0f
                    var holding = 0f
                    var today = 0f


                    it.forEach {
                        amount += it.持有额?.toFloat() ?: 0f
                        holding += it.持有收益?.toFloat() ?: 0f
                        today += it.估算收益?.toFloat() ?: 0f
                    }
                    binding.amount.apply {
                        setTextColor(Color.RED)
                        String.format("%.2f", amount).let {
                            text = it
                        }
                    }


                    //持有收益率
                    (String.format("%.2f", (holding / amount)*100) + "%").also {
                        binding.holding.apply {
                            text = it
                            if (it.contains("-")) {
                                setTextColor(Color.GREEN)
                            } else {
                                setTextColor(Color.RED)
                            }
                        }
                    }

                    //持有收益
                    String.format("%.2f", holding).also {
                        binding.holdingD.apply {
                            text = it
                            if (it.contains("-")) {
                                setTextColor(Color.GREEN)
                            } else {
                                setTextColor(Color.RED)
                            }
                        }

                    }

                    //今日收益
                    String.format("%.2f", today).also {
                        binding.todayD.apply {
                            text = it
                            if (it.contains("-")) {
                                setTextColor(Color.GREEN)
                            } else {
                                setTextColor(Color.RED)
                            }
                        }
                    }

                    //今日收益率
                    (String.format("%.2f", (today / amount)*100) + "%").also {
                        binding.today.apply {
                            text = it
                            if (it.contains("-")) {
                                setTextColor(Color.GREEN)
                            } else {
                                setTextColor(Color.RED)
                            }
                        }
                    }

                }

            })
        }


    }


//    var x1 = 0f
//    var y1 = 0f
//    var x2 = 0f
//    var y2 = 0f
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        //继承了Activity的onTouchEvent方法，直接监听点击事件
//        if (event != null) {
//            if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                //当手指按下的时候
//                x1 = event.getX();
//                y1 = event.getY();
//            }
//        }
//        if (event != null) {
//            if(event.getAction() == MotionEvent.ACTION_UP) {
//                //当手指离开的时候
//                x2 = event.getX();
//                y2 = event.getY();
//                if(y1 - y2 > 50) {
//                    Toast.makeText(requireContext(), "向上滑", Toast.LENGTH_SHORT).show();
//                } else if(y2 - y1 > 50) {
//                    Toast.makeText(requireContext(), "向下滑", Toast.LENGTH_SHORT).show();
//                } else if(x1 - x2 > 50) {
//                    Toast.makeText(requireContext(), "向左滑", Toast.LENGTH_SHORT).show();
//                } else if(x2 - x1 > 50) {
//                    Toast.makeText(requireContext(), "向右滑", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        return super.onTouchEvent(event)
//
//    }
//


    fun showAddDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        val builder = AlertDialog.Builder(requireContext())
        val dialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add, null)
        builder.setView(dialogView)
        val dialog = builder.show()
        val button = dialogView.findViewById<Button>(R.id.button)
        val editText1: TextInputEditText = dialogView.findViewById(R.id.code)
        val editText2: TextInputEditText = dialogView.findViewById(R.id.cost)
        val editText3: TextInputEditText = dialogView.findViewById(R.id.quantity)
        button.setOnClickListener { view: View? ->

            val s1 = editText1.text.toString()
            val s2 = editText2.text.toString()
            val s3 = editText3.text.toString()
            if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
                Snackbar.make(requireView(), "不能为空", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            } else {
                val cost = s2.toDouble()
                val quantity = s3.toDouble()
                val foudInfoEntity = FoudInfoEntity(s1, quantity, cost)
//                val repository = DbRepository()
//                var amount = 0
//                val codes: List<String> = repository.GetCodes()
//                if (codes.isNotEmpty()) {
//                    amount = codes.size
//                }
//                repository.InsertInfo(foudInfoEntity)
//                if (amount == repository.GetCodes().size) {
//                    Snackbar.make(requireView(), "添加失败", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
//                } else {
//                    Snackbar.make(requireView(), "添加成功", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
//                    dialog.dismiss()
//                }
                val foudInfoDao=KtDatabase.dataBase.getDao()
                lifecycleScope.launch(Dispatchers.IO){
                    with(foudInfoDao){
                       val amount= getCodes()?.size
                        insertFoudInfo(foudInfoEntity)
                        if (amount == getCodes()?.size) {
                            Snackbar.make(requireView(), "添加失败", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        } else {
                            Snackbar.make(requireView(), "添加成功", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            dialog.dismiss()
                        }
                    }

                }
            }
        }

        val importData = dialogView.findViewById<Button>(R.id.importFrom)
        importData.setOnClickListener {
                dialog.dismiss()
                val navController = Navigation.findNavController(requireView())
                navController.navigate(R.id.action_myFundFragment_to_importDataFragment)


        }
    }




}