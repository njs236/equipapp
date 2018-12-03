//
//  SecondViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import GoogleMaps

class MapViewController: UIViewController, GMSMapViewDelegate {

    override func loadView() {
        let camera = GMSCameraPosition.camera(withLatitude: -43.38791875701529, longitude: 172.64559879899025, zoom: 19.1)
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        
        view = mapView
        // Main Auditorium
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: -43.38747336005181, longitude: 172.6455733180046)
        marker.title = "Main Auditorium"
        marker.map = mapView
        
        let meetingArea = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: -43.38833393810643, longitude: 172.64617078006268)
        marker.title = "Food and Fellowship"
        meetingArea.map = mapView
        
        mapView.mapType = GMSMapViewType.hybrid
        
    }
    
    func mapView(_ mapView: GMSMapView, didLongPressAt coordinate: CLLocationCoordinate2D) {
        
    }


}

