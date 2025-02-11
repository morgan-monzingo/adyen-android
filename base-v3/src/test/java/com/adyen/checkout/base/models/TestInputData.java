/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by arman on 12/4/2019.
 */

package com.adyen.checkout.base.models;

import com.adyen.checkout.base.component.data.input.InputData;

public class TestInputData implements InputData {
    public boolean isValid = true;

    public TestInputData(boolean isValid) {
        this.isValid = isValid;
    }

    public TestInputData() {
    }
}
