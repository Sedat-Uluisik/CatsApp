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
        if(cat.image != null)
            if(cat.image.url != "")
                glide.load(cat.image.url).into(binding.catImageview)
            else
                binding.catImageview.setImageResource(R.drawable.error_24)
        else
            if (!cat.reference_image_id.isNullOrEmpty())
                glide.load("https://cdn2.thecatapi.com/images/${cat.reference_image_id}.jpg").into(binding.catImageview)

        binding.catName.text = cat.name ?: "-----"
        binding.descriptionTxt.text = cat.description ?: "-----"
        binding.ratingEnergyLevel.rating = cat.energy_level!!.toFloat()
        binding.groomingTxt.text = cat.grooming.toString()
        binding.lifeSpanTxt.text = cat.life_span.toString()

        viewModel.isFavorite(cat.id){
            if(it)
                binding.favoriteBtnDetails.setImageResource(R.drawable.favorite_32)
            else
                binding.favoriteBtnDetails.setImageResource(R.drawable.favorite_border_32)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}