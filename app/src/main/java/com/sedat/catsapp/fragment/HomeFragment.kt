package com.sedat.catsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedat.catsapp.R
import com.sedat.catsapp.adapter.HomeAdapter
import com.sedat.catsapp.databinding.FragmentHomeBinding
import com.sedat.catsapp.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var fragmentBinding: FragmentHomeBinding ?= null
    private val binding get() = fragmentBinding!!

    private lateinit var viewModel: HomeFragmentViewModel

    @Inject
    lateinit var homeAdapter: HomeAdapter

    private var isSearch: Boolean = false

    //api_key = eb572777-3a92-4cd4-9932-6767c362bea6
    //cat list = https://api.thecatapi.com/v1/breeds
    //search = https://api.thecatapi.com/v1/breeds/search  q=kedi_adı

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]

        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHome.adapter = homeAdapter

        viewModel.getData()

        var job: Job ?= null
        binding.searchEdittext.addTextChangedListener { editable->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(500)
                editable?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.search(it.toString())
                    }else
                        isSearch = true
                }
            }
        }

        binding.searchIcon.setOnClickListener {
            if(isSearch){
                viewModel.clearSearchItems()
                viewModel.getData()
                isSearch = false
            }
        }

        binding.favoriteBtn.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoritesFragment())
        }

        homeAdapter.catItemClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCatDetailsFragment(it))
        }
        homeAdapter.favoriteBtnClick { catItem, fav_btn->
            viewModel.isFavorite(catItem.id){
                if(it){
                    viewModel.deleteCatFromRoom(catItem.id)
                    fav_btn.setImageResource(R.drawable.favorite_border_32)
                }else{
                    viewModel.saveCatFromRoom(catItem)
                    fav_btn.setImageResource(R.drawable.favorite_32)
                }
            }
        }

        viewModel.getCatsFromRoom()

        observeData()
    }

    private fun observeData(){
        viewModel.search.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                lifecycleScope.launch {
                    homeAdapter.submitData(PagingData.from(it))
                }
            }
        }

        viewModel.catListFromRoom.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                homeAdapter.favorites = it
            }
        }

        homeAdapter.addLoadStateListener {
            fragmentBinding?.progressBarHome?.visibility = if(homeAdapter.itemCount == 0 || it.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            fragmentBinding?.recyclerHome?.visibility = if(homeAdapter.itemCount == 0 || it.refresh is LoadState.Loading) View.GONE else View.VISIBLE

        }

        viewModel.catList.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                it.collectLatest {
                    homeAdapter.submitData(PagingData.from(listOf()))
                    homeAdapter.submitData(it)
                }
            }
        }
    }

    /*private fun getCatList(){
        lifecycleScope.launch {
            viewModel.catList().collectLatest {
                homeAdapter.submitData(PagingData.from(listOf()))
                homeAdapter.submitData(it)
            }
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}