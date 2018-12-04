package com.nathan.equipapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nathan.equipapp.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnMapClickListener {
    val TAG = this.javaClass.simpleName
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var layout : View? = null
    private var mapFragment: SupportMapFragment? = null
    private var map : GoogleMap? = null
    private var myContext : Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        Log.d(TAG, "map started")
        map = googleMap
        val kaiapoiHighSchoolCoordinates = LatLng(-43.38791875701429,172.64559879899025)
        val mainAuditorium = LatLng(-43.38747336005181,172.6455733180046)
        val meetingArea = LatLng(-43.38833393810643,172.64617078006268)
        val speaker = "Dave Clancey is speaking from 1 Samuel 22"
        var marker = map?.addMarker(MarkerOptions().position(mainAuditorium).title("Main Auditorium"))
        marker = map?.addMarker(MarkerOptions().position(meetingArea).title("Food and Fellowship"))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(kaiapoiHighSchoolCoordinates, 19.1f))
        map?.mapType = GoogleMap.MAP_TYPE_HYBRID
        map?.setOnCameraMoveListener(this)
        map?.setOnMapClickListener(this)
        val thisContext = myContext
        if (thisContext != null) {

            if (ContextCompat.checkSelfPermission(thisContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                map?.setMyLocationEnabled(true)
            } else {
                // Show rationale and request permission.
                var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(permissions, 0)


            }
        }
        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }


    override fun onCameraMove() {

        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.

    }

    override fun onMapClick(latLng: LatLng?) {
        Log.d(TAG, latLng.toString())
        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        try {
            if (requestCode == 0) {
                if (permissions.size == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map?.setMyLocationEnabled(true)
                } else {

                }
            }
        } catch (ex: SecurityException) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "mapCreated")
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        return layout
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onMapInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onMapInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
