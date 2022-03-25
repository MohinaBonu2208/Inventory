package com.bignerdranch.android.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.inventory.data.Item
import com.bignerdranch.android.inventory.data.getFormattedPrice
import com.bignerdranch.android.inventory.databinding.FragmentDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailFragment : Fragment() {
    private val navigationArgs: DetailFragmentArgs by navArgs()
    lateinit var item: Item
    private val viewModel: InventorViewModel by activityViewModels {
        InventorViewModelFactory((activity?.application as InventoryApplication).database.itemDao())
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }

    private fun bind(item: Item) {
        binding.apply {
            name.text = getString(R.string.name_detail, item.itemName)
            price.text = getString(R.string.price_detail, item.getFormattedPrice() )
            quantity.text = getString(R.string.quantity_detail, item.quantity.toString())

            sell.isEnabled = viewModel.isStockAvailable(item)
            sell.setOnClickListener { viewModel.sell(item) }

            delete.setOnClickListener { showDeleteDialog() }

            edit.setOnClickListener { editItem() }
        }
    }

    private fun delete(item: Item) {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    private fun editItem() {
        val action = DetailFragmentDirections.actionDetailFragmentToAddFragment(getString(R.string.edit_item), item.id)
        findNavController().navigate(action)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_delete_title)
            .setTitle(R.string.attention)
            .setCancelable(false)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ -> delete(item) }.show()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}