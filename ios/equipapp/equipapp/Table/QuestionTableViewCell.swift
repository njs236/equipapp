//
//  QuestionTableViewCell.swift
//  equipapp
//
//  Created by Nathan Sinclair on 4/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit

class QuestionTableViewCell: UITableViewCell {

    @IBOutlet var authorLabel: UILabel!
    @IBOutlet var questionLabel: UILabel!
    @IBOutlet var speakerLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
