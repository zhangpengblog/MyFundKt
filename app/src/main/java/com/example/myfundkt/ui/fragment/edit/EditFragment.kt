package com.example.myfundkt.ui.fragment.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myfundkt.databinding.EditFragmentBinding
import com.example.myfundkt.ui.MyViewModel

class EditFragment : Fragment() {
    private lateinit var binding: EditFragmentBinding
    private lateinit var myViewModel: MyViewModel

    companion object;

    private lateinit var viewModel: EditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        myViewModel.infomationCode.observe(viewLifecycleOwner, {
            it?.let {
                binding.code.text = it
                viewModel.find(it)
            }
        })
        // TODO: Use the ViewModel
        binding.button.setOnClickListener {
            viewModel.foudInfoEntity.value?.let {
                it.quantity = binding.quantity.text.toString().toDouble()
                it.cost = binding.cost.text.toString().toDouble()
                viewModel.update(it)
            }
        }

        viewModel.apply {
            foudInfoEntity.observe(viewLifecycleOwner, Observer {
                it?.let {
                    binding.quantity.setText(it.quantity.toString())
                    binding.cost.setText(it.cost.toString())
                }
            })
        }


    }

}