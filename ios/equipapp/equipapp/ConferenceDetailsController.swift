//
//  FirstViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import WebKit
import Foundation

class ConferenceDetailsController: UIViewController {

    var webView: WKWebView!
    
    var fileAsString : String?
    
    @IBAction func registerSouth(_ sender: Any) {
        
        // open web page with south island registration page
        let url = "http://www.equipconference.org.nz/registration-1-1/"
        UIApplication.shared.open(NSURL(string: url)! as URL, options: [:], completionHandler: nil)
    }
    @IBOutlet var playerView: YTPlayerView!
    @IBOutlet var Speaker1Info: UILabel!
    @IBOutlet var Speaker2Info: UILabel!
    @IBOutlet var Speaker1Image: UIImageView!
    @IBOutlet var Speaker2Image: UIImageView!
    @IBOutlet var EquipSouthBtn: UIButton!
    override func loadView() {
        super.loadView()
            }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
         let conferenceURL = "vQ2x4R9r7QA"
        let seconds : Float = 0.0
        let quality = YTPlaybackQuality.HD720
        self.playerView.loadVideo(byId: conferenceURL, startSeconds: seconds, suggestedQuality: quality)
        
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        readSpeakerInfo()
    }
    
    func readSpeakerInfo() {
        let destPathName = "speaker_info.txt"
        let filePath = Bundle.main.url(forResource: "speaker_info", withExtension: "txt")
        
        guard let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).last else{
            fatalError("No document directory found in application bundle.")
        }
        let writeableFileURL = dir.appendingPathComponent(destPathName)
        
        if (try? writeableFileURL.checkResourceIsReachable()) == nil {
            
            
            do {
                self.fileAsString = try String(contentsOf: filePath!, encoding: .utf8)
            }
            catch {
                print("error:", error)
            }
        }
        
        // parse the string into an array
        var lines: [String] = []
        fileAsString?.enumerateLines { line, _ in
            lines.append(line)
        }
        
        //replace "/" character with newline
        let speaker1text = lines[0].replacingOccurrences(of: "/", with: "\n")
        print("text: " + speaker1text)
        let speaker2text = lines[1].replacingOccurrences(of: "/", with: "\n")
        
        
        Speaker1Info.text = speaker1text
        Speaker2Info.text = speaker2text
        Speaker1Info.numberOfLines = 0
        Speaker2Info.numberOfLines = 0
        Speaker1Info.sizeToFit()
        Speaker2Info.sizeToFit()
    }


}

