//
//  CustomTableViewCell.swift
//  equipapp
//
//  Created by Nathan Sinclair on 3/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import FontAwesome_swift


class CustomTableViewCell: UITableViewCell {
    
    
 
    var cellDelegate: RecordingCellProtocol?
    var index: Int?
    @IBOutlet var speakerOfRecording: UILabel!
    @IBOutlet var playButton: UIButton!
    @IBOutlet var dateOfRecording: UILabel!
    @IBOutlet var nameOfRecording: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        let font = UIFont.fontAwesome(ofSize: 15, style: FontAwesomeStyle.solid)
        let playText = String.fontAwesomeIcon(name: FontAwesome.playCircle)
        self.playButton.titleLabel?.font = font
        let stopText = String.fontAwesomeIcon(name: FontAwesome.stopCircle)
        self.playButton.setTitle(playText, for: UIControl.State.normal)
        self.playButton.setTitle(stopText, for: UIControl.State.selected)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    @IBAction func playButtonTouchUpInside(_ sender: UIButton) {
        self.playButton.isSelected = !self.playButton.isSelected
        // send button to playing of resource in delegate.
        self.cellDelegate?.didTapCell(cell: self, index: index!)
    }
    
    
}
