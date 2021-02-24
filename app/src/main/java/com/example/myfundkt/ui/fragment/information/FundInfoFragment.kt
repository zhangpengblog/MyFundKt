package com.example.myfundkt.ui.fragment.information

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myfundkt.R
import com.example.myfundkt.databinding.FundInfoFragmentBinding
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.ui.lineChart.LineChart

private const val TAG = "FundInfoFragment"
private lateinit var lineChart: LineChart

class FundInfoFragment : Fragment() {
    private lateinit var myViewModel: MyViewModel
    private lateinit var binding: FundInfoFragmentBinding
    private lateinit var viewModel: FundInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "详情"
        binding = FundInfoFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = view.findViewById(R.id.line_chart)
        mutableListOf<LineChart.Data<String>>().apply {
            add(LineChart.Data("09:30"))
            for (i in 1..240) {
                add(LineChart.Data(""))
            }
            add(LineChart.Data("15:30"))
            lineChart.setXAxisBasisData(this)

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FundInfoViewModel::class.java).apply {
            progressBarVisibility.observe(viewLifecycleOwner, {
                binding.progressBar.visibility = it
            })
        }
        Log.d(TAG, "onActivityCreated:code ")
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        myViewModel.infomationCode.observe(requireActivity(), {
            Log.d(TAG, "onActivityCreated:code $it")
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
            it?.let { it1 ->
                lineChart.setMidStart(it1)
            }


        })

        viewModel.expansion.observe(viewLifecycleOwner, {
            binding.fundTitle.text = ""
            it?.let { it1 ->
                (it1.sHORTNAME + "(" + it1.fCODE + ")").also { it2 -> binding.fundTitle.text = it2 }
            }


        })
    }

}