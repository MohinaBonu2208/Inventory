package com.bignerdranch.android.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.inventory.databinding.FragmentListBinding


class ListFragment : Fragment() {
//    private val viewModel: InventorViewModel by activityViewModels {
//        InventorViewModelFactory((activity?.application as InventoryApplication).database.itemDao())
//    }
private val viewModel: InventorViewModel by activityViewModels {
    InventorViewModelFactory(
        (activity?.application as InventoryApplication).database.itemDao()
    )
}

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(it.id)
            findNavController().navigate(action)

        }

        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        binding.rView.adapter = adapter
        binding.rView.layoutManager = LinearLayoutManager(requireContext())
        binding.floatingButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddFragment(getString(R.string.add_new_item))
            this.findNavController().navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}