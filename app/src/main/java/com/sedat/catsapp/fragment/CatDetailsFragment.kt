package com.sedat.catsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.sedat.catsapp.R
import com.sedat.catsapp.databinding.FragmentCatDetailsBinding
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.util.Util.IMAGE_URL
import com.sedat.catsapp.viewmodel.CatDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CatDetailsFragment : Fragment() {

    private var fragmentBinding: FragmentCatDetailsBinding ?= null
    private val binding get() = fragmentBinding!!

    @Inject
    lateinit var glide: RequestManager

    private lateinit var viewModel: CatDetailsViewModel

    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentCatDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CatDetailsViewModel::class.java]

        arguments?.let {
            val catItem = CatDetailsFragmentArgs.fromBundle(it).catItem
            if(catItem != null)
                bind(catItem)
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bind(cat: CatItem){

        glide.load("$IMAGE_URL/${cat.reference_image_id}.jpg").into(binding.catImageview)

        binding.catName.text = cat.name?.uppercase() ?: "-----"
        binding.descriptionTxt.text = cat.description ?: "-----"
        binding.ratingEnergyLevel.rating = cat.energy_level!!.toFloat()
        binding.ratingAdaptability.rating = cat.adaptability!!.toFloat()
        binding.ratingGrroming.rating = cat.grooming!!.toFloat()
        binding.lifeSpanTxt.text = cat.life_span.toString()

        viewModel.isFavorite(cat.id){
            if(it){
                isFavorite = true
                binding.favoriteBtnDetails.setImageResource(R.drawable.favorite_32)
            }
            else{
                isFavorite = false
                binding.favoriteBtnDetails.setImageResource(R.drawable.favorite_border_32)
            }
        }

        binding.favoriteBtnDetails.setOnClickListener {
            if(isFavorite){
                viewModel.deleteCatFromRoomWithId(cat.id)
                binding.favoriteBtnDetails.setImageResource(R.drawable.favorite_border_32)
                isFavorite = false
            }else{
                viewModel.saveCatFromRoom(cat)
                binding.favoriteBtnDetails.setImageResource(R.drawable.favorite_32)
                isFavorite = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}