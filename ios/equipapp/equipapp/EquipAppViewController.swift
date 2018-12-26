//
//  EquipAppViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 27/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit

class EquipAppViewController: UITabBarController {
    
    @IBInspectable var defaultIndex: Int = 0

    override func viewDidLoad() {
        super.viewDidLoad()
        selectedIndex = defaultIndex
        
 

        // Do any additional setup after loading the view.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
