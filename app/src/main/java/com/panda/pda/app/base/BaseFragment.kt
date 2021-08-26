package com.panda.pda.app.base

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.core.*
import timber.log.Timber

/**
 * created by AnJiwei 2020/10/12
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private var permissionEmit: CompletableEmitter? = null
    private var openFileEmit: SingleEmitter<Uri>? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestOpenFileLauncher: ActivityResultLauncher<Array<String>>
    protected val TAG get() = javaClass.simpleName
    protected val sp: SharedPreferences by lazy {
        this.requireContext().getSharedPreferences("SOG", Context.MODE_PRIVATE)
    }

    protected val navController by lazy { findNavController() }

    protected val navBackListener =  { _: View -> navController.popBackStack()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate ${javaClass.simpleName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                permissionEmit?.onComplete()
            } else {
                permissionEmit?.onError(Exception("请允许"))
            }
        }

        requestOpenFileLauncher =
            registerForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri == null) {
                    openFileEmit?.onError(NullPointerException())
                } else {
                    openFileEmit?.onSuccess(uri)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy ${javaClass.simpleName}")
    }

    protected fun permissionRequest(permission: String): Completable {
        return Completable.create { emit ->
            if (ContextCompat.checkSelfPermission(requireContext(),
                    permission) == PackageManager.PERMISSION_GRANTED
            ) {
                emit.onComplete()
            } else {
                permissionEmit = emit
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    protected fun openFileRequest(fileTypes: Array<String>): Single<Uri> {
        return permissionRequest(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .andThen(Single.create { emit ->
                openFileEmit = emit
                requestOpenFileLauncher.launch(fileTypes)
            })
    }
}