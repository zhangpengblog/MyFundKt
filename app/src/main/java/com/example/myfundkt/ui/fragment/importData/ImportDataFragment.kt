package com.example.myfundkt.ui.fragment.importData

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myfundkt.R
import com.example.myfundkt.databinding.ImportDataFragmentBinding
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.utils.ToastUtil
import com.google.android.material.snackbar.Snackbar

private const val TAG = "ImportDataFragment"
class ImportDataFragment : Fragment() {

private lateinit var binding: ImportDataFragmentBinding
    companion object {
        fun newInstance() = ImportDataFragment()
    }
    private lateinit var myViewModel: MyViewModel
    private lateinit var viewModel: ImportDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "导入"
        binding = ImportDataFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener {
            binding.editTextTextMultiLine.text.toString().also {
                if (it.isEmpty()){
                    ToastUtil.show("请粘贴内容")
                    return@setOnClickListener
                }
                importData(it)
            }
        }
    }

    private fun importData(code: String) {
        viewModel?.let {
            it.fromImport(code)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ImportDataViewModel::class.java).apply {
            nowIndex.observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "onActivityCreated: "+it+"/"+_count)
                if (_count!=0 && _count==it){
                    view?.let { it1 ->
                        Snackbar.make(it1, "导入成功", Snackbar.LENGTH_SHORT).show()
                        myViewModel.initSellectionFund()
                        val controller: NavController? = getView()?.let { Navigation.findNavController(it) }
                        if (controller != null) {
                            controller.navigate(R.id.action_importDataFragment_to_myFundFragment)
                        }
                    }
                }
            })
        }
        // TODO: Use the ViewModel
    }

}