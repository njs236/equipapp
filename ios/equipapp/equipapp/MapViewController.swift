//
//  SecondViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import GoogleMaps
import Firebase

class MapViewController: UIViewController, GMSMapViewDelegate {

    override func loadView() {
        let db = Firestore.firestore()
        let camera = GMSCameraPosition.camera(withLatitude: -43.38791875701529, longitude: 172.64559879899025, zoom: 19.1)
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
         mapView.mapType = GMSMapViewType.hybrid
        db.collection("maps").getDocuments { (documentSnapshot, error) in
            for document in documentSnapshot!.documents {
                if (document.get("center") != nil) {
                    let position = document.get("position") as! GeoPoint
                    let lat = position.latitude
                    let lng = position.longitude
                    let zoom = document.get("zoom") as! NSNumber
                    let cameraPosition = GMSCameraPosition.camera(withLatitude: lat, longitude: lng, zoom: zoom.floatValue)
                    let move = GMSCameraUpdate.setCamera(cameraPosition)
                    mapView.moveCamera(move)
                } else {
                    let marker = GMSMarker()
                    let position = document.get("position") as! GeoPoint
                    let lat = position.latitude
                    let lng = position.longitude
                    let title = document.get("title") as! String
                    marker.position = CLLocationCoordinate2DMake(lat, lng)
                    marker.title = title
                    marker.map = mapView
                }
            }
        }
        
       
        
        view = mapView
        // Main Auditorium
        
       
        
    }
    


}

