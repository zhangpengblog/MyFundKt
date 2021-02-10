package com.example.myfundkt.ui.fragment.myfund

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.utils.MyLog
import com.example.myfundkt.utils.ToastUtil


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
        binding.root.setOnRefreshListener {
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
                val fund = selectionData[position]
                val code = fund.代码
                val repository = DbRepository()
                val entity = repository.FindByCode(code)
                val id = entity.id
                 activity?.let {
                    AlertDialog.Builder(it).setMessage("删除").setPositiveButton("删除",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            repository.DeleteById(id)
                            selectionAdapter.removeAt(position)
                            selectionAdapter.notifyDataSetChanged()
                        })
                }?.setNegativeButton("取消", null)
                    ?.create()?.show()
                return true
            }
        })


    }

    private fun refreshData() {
        myViewModel.let {
            it.initCode()
            it.initIndexFund()
            it.initSellectionFund()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java).apply {

            initIndexFund()
            initSellectionFund()
            getHoliday()

            progressBarVisibility.observe(viewLifecycleOwner, {
                binding.progressBar.visibility = it
            })

            topLiveData.observe(viewLifecycleOwner, {
                binding.root.finishRefresh()
                Log.d(TAG, "onActivityCreated:topLiveData "+it)
                topData.clear()
                topData.addAll(it)
                topAdapter.notifyDataSetChanged()

            })
            collection.observe(viewLifecycleOwner, {
                binding.root.finishRefresh()
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






}