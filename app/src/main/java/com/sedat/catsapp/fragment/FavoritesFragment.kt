package com.sedat.catsapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedat.catsapp.R
import com.sedat.catsapp.adapter.FavoriteAdapter
import com.sedat.catsapp.adapter.HomeAdapter
import com.sedat.catsapp.databinding.FragmentFavoritesBinding
import com.sedat.catsapp.viewmodel.FavoritesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var fragmentBinding: FragmentFavoritesBinding ?= null
    private val binding get() = fragmentBinding!!

    @Inject
    lateinit var adapter: FavoriteAdapter

    private lateinit var viewModel: FavoritesFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       fragmentBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FavoritesFragmentViewModel::class.java]

        binding.recyclerFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFav.adapter = adapter

        viewModel.getDataFromRoom()

        binding.backBtnFavorites.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter.catItemClick {
            findNavController().navigate(FavoritesFragmentDirections.actionFavoritesFragmentToCatDetailsFragment(it))
        }
        adapter.favoriteBtnClick { catItem, fav_btn->
            viewModel.deleteCatFromRoom(catItem.id)
            fav_btn.setImageResource(R.drawable.favorite_border_32)
        }

        observeData()

    }

    private fun observeData(){
        viewModel.catList.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                adapter.submitData(PagingData.from(it))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}