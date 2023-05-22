package com.test.archi.ui.user

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.test.archi.bind.FragmentDataBindingComponent
import com.test.archi.ui.common.RepoListAdapter
import com.test.archi.ui.common.RetryCallback
import com.test.archi.utils.autoCleared
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.archi.AppExecutors
import com.test.archi.R
import com.test.archi.databinding.UserFragmentBinding
import com.test.archi.ui.base.BaseFragment
import com.test.archi.ui.user.UserFragmentArgs
import com.test.archi.ui.user.UserFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : BaseFragment<UserViewModel>() {

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<UserFragmentBinding>()
    lateinit var dataBindingComponent: DataBindingComponent;

    private val userViewModel: UserViewModel by viewModels()

    private val params by navArgs<UserFragmentArgs>()
    private var adapter by autoCleared<RepoListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBindingComponent = FragmentDataBindingComponent(this)
        val dataBinding = DataBindingUtil.inflate<UserFragmentBinding>(
            inflater,
            R.layout.user_fragment,
            container,
            false,
            dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                userViewModel.retry()
            }
        }
        binding = dataBinding
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.move)
        // When the image is loaded, set the image request listener to start the transaction
        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
            }
        }
        // Make sure we don't wait longer than a second for the image request
        postponeEnterTransition(1, TimeUnit.SECONDS)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel.setLogin(params.login)
        binding.args = params

        binding.user = userViewModel.user
        binding.lifecycleOwner = viewLifecycleOwner
        val rvAdapter = RepoListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors,
            showFullName = false
        ) { repo ->
            findNavController().navigate(
                UserFragmentDirections.showRepo(
                    repo.owner.login,
                    repo.name
                )
            )
        }
        binding.repoList.adapter = rvAdapter
        this.adapter = rvAdapter
        initRepoList()
    }

    private fun initRepoList() {
        userViewModel.repositories.observe(viewLifecycleOwner, Observer { repos ->
            adapter.submitList(repos?.data)
        })
    }
}
