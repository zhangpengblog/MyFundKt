package com.example.myfundkt.ui.fragment.information

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myfundkt.R
import com.example.myfundkt.databinding.FundInfoFragmentBinding
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.ui.lineChart.LineChart

private const val TAG = "FundInfoFragment"
private lateinit var lineChart: LineChart
private lateinit var binding: FundInfoFragmentBinding
class FundInfoFragment : Fragment() {
    private lateinit var myViewModel: MyViewModel
//    companion object {
//        fun newInstance() = FundInfoFragment()
//    }

    private lateinit var viewModel: FundInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "详情"
        binding = FundInfoFragmentBinding.inflate(inflater,container,false)
//        return inflater.inflate(R.layout.fund_info_fragment, container, false)
      return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = view.findViewById(R.id.line_chart)
        val _dataList=mutableListOf<LineChart.Data<String>>()
        _dataList.add(LineChart.Data("09:30"))
        for (i in 1..240) {
            _dataList.add(LineChart.Data(""))
        }
        _dataList.add(LineChart.Data("15:30"))

        _dataList.let {
            lineChart.setXAxisBasisData(it)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FundInfoViewModel::class.java).apply {
            progressBarVisibility.observe(viewLifecycleOwner, Observer {
               binding.progressBar.visibility = it
            })
        }
        var code: String?
        Log.d(TAG, "onActivityCreated:code ")
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        myViewModel.infomationCode.observe(requireActivity(), {
            Log.d(TAG, "onActivityCreated:code "+it)
                it?.let {
                    viewModel.initSellectionFundCoro(it)
                }
            })


        // TODO: Use the ViewModel

        viewModel.data.observe(viewLifecycleOwner, {
            it?.let { it1 ->
                lineChart.setData(it1)
            }


        })

        viewModel.start.observe(viewLifecycleOwner, {
            it?.let { it1->
                lineChart.setMidStart(it1)
            }


        })

        viewModel.expansion.observe(viewLifecycleOwner, {
                it?.let { it1->
                    (it1.sHORTNAME+"("+it1.fCODE+")").also { binding.fundTitle.text = it }
                }


        })
    }

}