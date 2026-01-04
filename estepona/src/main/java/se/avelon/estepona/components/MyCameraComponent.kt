/*
 * Copyright 2025 David Johansson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.avelon.estepona.components

import android.content.Context
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.avelon.estepona.logging.DLog

class MyCameraComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyCameraComponent::class.java)
    }

    // Used to set up a link between the Camera and your UI.
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private val cameraPreviewUseCase =
        Preview.Builder().build().apply {
            setSurfaceProvider { newSurfaceRequest -> _surfaceRequest.update { newSurfaceRequest } }
        }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)

        if (processCameraProvider.hasCamera(DEFAULT_FRONT_CAMERA)) {
            processCameraProvider.bindToLifecycle(
                lifecycleOwner,
                DEFAULT_FRONT_CAMERA,
                cameraPreviewUseCase,
            )

            // Cancellation signals we're done with the camera
            try {
                awaitCancellation()
            } finally {
                processCameraProvider.unbindAll()
            }
        } else {
            DLog.warning(TAG, "No front camera found")
        }
    }

    fun setU(cameraSelector: CameraSelector) {}
}

@Composable
fun MyCamera(modifier: Modifier) {
    DLog.method(MyCameraComponent.TAG, "MyCamera()")

    Column(
        modifier = modifier.fillMaxSize().wrapContentSize().widthIn(max = 480.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Hello world", textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        var viewModel = viewModel<MyCameraComponent>()
        Box(modifier) {
            CameraPreviewContent(viewModel)
            Button(
                onClick = {
                    DLog.info(MyCameraComponent.TAG, "Button()")
                    viewModel.setU(DEFAULT_BACK_CAMERA)
                }
            ) {
                Text("Rotate")
            }
        }
    }
}

@Composable
fun CameraPreviewContent(
    viewModel: MyCameraComponent,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    DLog.method(MyCameraComponent.TAG, "CameraPreviewContent()")

    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    surfaceRequest?.let { request ->
        CameraXViewfinder(surfaceRequest = request, modifier = modifier)
    }
}
