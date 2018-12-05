//
//  CustomTableViewCell.swift
//  equipapp
//
//  Created by Nathan Sinclair on 3/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit

class CustomTableViewCell: UITableViewCell {
    
    
 
    var cellDelegate: RecordingCellProtocol?
    var index: Int?
    @IBOutlet var playButton: UIButton!
    @IBOutlet var dateOfRecording: UILabel!
    @IBOutlet var nameOfRecording: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @IBAction func playButtonSelected(_ sender: UIButton) {
        // send button to playing of resource in delegate.
        self.cellDelegate?.didTapCell(cell: self, index: index!)
    }
    
    
}
