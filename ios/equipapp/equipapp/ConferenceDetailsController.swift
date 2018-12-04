//
//  FirstViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import WebKit

class ConferenceDetailsController: UIViewController {
    
    @IBAction func sendButtonClick(_ sender: Any) {
    }
    @IBOutlet var putWebView: UIView!
    var webView: WKWebView!
    
    override func loadView() {
        super.loadView()
        let config = WKWebViewConfiguration()
        webView = WKWebView.init(frame: CGRect(x: putWebView.frame.minX, y: putWebView.frame.minY, width: putWebView.frame.width, height: putWebView.frame.height), configuration: config)
        putWebView.addSubview(webView)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        let conferenceURL = URL.init(fileURLWithPath: "https://youtu.be/vQ2x4R9r7QA")
        let request = URLRequest.init(url: conferenceURL)
        webView.load(request)
    }


}

