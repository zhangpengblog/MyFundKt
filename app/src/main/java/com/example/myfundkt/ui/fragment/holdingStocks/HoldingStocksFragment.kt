package com.example.myfundkt.ui.fragment.holdingStocks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfundkt.bean.holdingStocks.FundStock
import com.example.myfundkt.bean.holdingStocks.HoldingData
import com.example.myfundkt.bean.top.Diff
import com.example.myfundkt.databinding.HoldingStocksFragmentBinding
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.ui.fragment.holdingStocks.adapter.HoldingStocksAdapter
import com.example.myfundkt.ui.fragment.information.FundInfoViewModel


private const val TAG = "HoldingStocksFragment"
class HoldingStocksFragment : Fragment() {
private lateinit var binding: HoldingStocksFragmentBinding
    companion object {
        fun newInstance() = HoldingStocksFragment()
    }

    private lateinit var viewModel: HoldingStocksViewModel
    private val list:MutableList<HoldingData> = mutableListOf()
    private val adapter: HoldingStocksAdapter = HoldingStocksAdapter(list)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HoldingStocksFragmentBinding.inflate(inflater,container,false)
//        return inflater.inflate(R.layout.holding_stocks_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stocksRecycler.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.stocksRecycler.adapter  = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HoldingStocksViewModel::class.java).apply {
            progressBarVisibility.observe(viewLifecycleOwner, Observer {
               binding.progressBar.visibility = it
            })
        }
        val myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        val fundInfoViewModel = ViewModelProvider(requireActivity()).get(FundInfoViewModel::class.java)
        // get code
        myViewModel.infomationCode.observe(requireActivity(), {
            Log.d(TAG, "onActivityCreated:code "+it)
            it?.let {
                viewModel.getStocks(it)
            }
        })
        //exist name
        fundInfoViewModel.expansion.observe(viewLifecycleOwner, Observer {
            it?.let { it1->
                Log.d(TAG, "onActivityCreated:expansion "+it1)
                (it1.sHORTNAME+"("+it1.fCODE+")").also { binding.appCompatTextView.text = it }
            }

        })
        // TODO: Use the ViewModel
        viewModel.apply {

            holdingData.observe(viewLifecycleOwner, Observer {
                it?.let {
                    Log.d(TAG, "onActivityCreated:holdingData "+it)
                    list.clear()
                    list.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

}