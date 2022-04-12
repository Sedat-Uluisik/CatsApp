package com.sedat.catsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.sedat.catsapp.HomeAdapter
import com.sedat.catsapp.R
import com.sedat.catsapp.databinding.FragmentHomeBinding
import com.sedat.catsapp.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.collect
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

        lifecycleScope.launch {
            viewModel.catList().collectLatest {
                homeAdapter.submitData(it)
            }
        }

        homeAdapter.catItemClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCatDetailsFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}