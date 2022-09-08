package com.example.samplear

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private var modelRenderable: ModelRenderable? = null
    private lateinit var arFragment: CustomArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = supportFragmentManager.findFragmentById(R.id.fragment_ar) as CustomArFragment
        downloadModel("https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf")
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            if (modelRenderable == null) return@setOnTapArPlaneListener
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            TransformableNode(arFragment.transformationSystem).apply {
                renderable = modelRenderable
                scaleController.minScale = 0.06f
                scaleController.maxScale = 1.0f
                worldScale = Vector3(0.5f, 0.5f, 0.5f)
                setParent(anchorNode)
                select()
            }
        }
    }

    private fun downloadModel(url_model3d: String) {
        val renderable = RenderableSource.builder()
            .setSource(this, Uri.parse(url_model3d), RenderableSource.SourceType.GLTF2)
            .setRecenterMode(RenderableSource.RecenterMode.CENTER)
            .build()
        ModelRenderable.builder()
            .setSource(this, renderable)
            .build()
            .thenAccept { mRenderable ->
                modelRenderable = mRenderable
                Toast.makeText(this@MainActivity, "Descarga Completa: Toque un superficie",Toast.LENGTH_LONG).show()
            }
            .exceptionally {
                Toast.makeText(this@MainActivity, "No se pudo Descargar: Compruebe su Red",Toast.LENGTH_LONG).show()
                return@exceptionally null
            }
    }
}