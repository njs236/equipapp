//
//  PlayerView.swift
//  equipapp
//
//  Created by Nathan Sinclair on 5/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import AVFoundation

class PlayerView: UIView {

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    
    var player: AVPlayer {
        get {
            return playerLayer.player!
        }
        set {
            playerLayer.player = newValue
        }
    }
    
    var playerLayer: AVPlayerLayer {
        return layer as! AVPlayerLayer
    }
    
    override static var layerClass: AnyClass {
        return AVPlayerLayer.self
    }

}
