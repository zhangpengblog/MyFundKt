package com.example.myfundkt.ui.fragment.importData

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myfundkt.R
import com.example.myfundkt.databinding.ImportDataFragmentBinding
import com.example.myfundkt.ui.MyViewModel
import com.example.myfundkt.utils.ToastUtil
import com.example.myfundkt.utils.ktDao
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

private const val TAG = "ImportDataFragment"

class ImportDataFragment : Fragment() {

    private lateinit var binding: ImportDataFragmentBinding

    companion object;

    private lateinit var myViewModel: MyViewModel
    private lateinit var viewModel: ImportDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "导入"
        binding = ImportDataFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener {
            binding.editTextTextMultiLine.text.toString().also {
                if (it.isEmpty()) {
                    ToastUtil.show("请粘贴内容")
                    return@setOnClickListener
                }

//                    val repository = DbRepository()
//                    repository.ClearAll()
//                    importData(it)
                lifecycleScope.launch {
                    ktDao.clear()
                    importData(it)
                }


            }
        }
    }

    private fun importData(code: String) {
        viewModel.fromImport(code)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ImportDataViewModel::class.java).apply {
            nowIndex.observe(viewLifecycleOwner, {
                Log.d(TAG, "onActivityCreated: $it/$_count")
                if (_count != 0 && _count == it) {
                    view?.let { it1 ->
                        Snackbar.make(it1, "导入成功", Snackbar.LENGTH_SHORT).show()

                        val controller: NavController? =
                            view?.let { Navigation.findNavController(it) }
                        if (controller != null) {
                            controller.navigate(R.id.action_importDataFragment_to_myFundFragment)
                            myViewModel.initSelectedFundCoro()
                        }
                    }
                }
            })
        }
        // TODO: Use the ViewModel
    }

}