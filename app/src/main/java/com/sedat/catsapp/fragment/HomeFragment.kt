package com.sedat.catsapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerHome.adapter = homeAdapter

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
                observeData()
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
           if(catItem != null){
               viewModel.isFavorite(catItem.id){
                   if(it){
                       viewModel.deleteCatFromRoomWithId(catItem.id)
                       fav_btn.setImageResource(R.drawable.favorite_border_32)
                   }else{
                       viewModel.saveCatFromRoom(catItem)
                       fav_btn.setImageResource(R.drawable.favorite_32)
                   }
               }
           }
        }
        observeData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData(){

        println("observe fun çalıştı in home fragment") //tek sefer

        viewModel.search.observe(viewLifecycleOwner){ searchList->
            if(searchList.isNotEmpty()){
                viewModel.getCatsFromRoom {
                    lifecycleScope.launch {

                        println("observe searching data from api in home fragment")

                        homeAdapter.favorites = it
                        homeAdapter.submitData(PagingData.from(searchList))
                    }
                }
            }
        }

        viewModel.getCatsFromRoom {

            println("get cats from room in home fragment") //tek sefer

            homeAdapter.favorites = it
            viewModel.getData()
        }

        homeAdapter.addLoadStateListener {
            binding.progressBarHome.visibility = if(homeAdapter.itemCount == 0 || it.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            binding.recyclerHome.visibility = if(homeAdapter.itemCount == 0 || it.refresh is LoadState.Loading) View.GONE else View.VISIBLE
        }

        viewModel.catList.observe(viewLifecycleOwner){

            //buradaki kontrol, findNavController().popBackStack() yapıldığında observe işleminin iki defa çalışmasını engelliyor.

            if(viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED){
                lifecycleScope.launch {
                    it.collectLatest {

                        println("observe cat list from api in home fragment") //tek sefer

                        homeAdapter.submitData(PagingData.from(listOf()))
                        homeAdapter.submitData(it)
                        homeAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}