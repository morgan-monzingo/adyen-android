/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by caiof on 5/4/2019.
 */

package com.adyen.checkout.redirect;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.adyen.checkout.base.ActionComponentProvider;
import com.adyen.checkout.base.component.BaseActionComponent;
import com.adyen.checkout.base.component.ActionComponentProviderImpl;
import com.adyen.checkout.base.model.payments.response.Action;
import com.adyen.checkout.base.model.payments.response.RedirectAction;
import com.adyen.checkout.core.exeption.CheckoutException;
import com.adyen.checkout.core.exeption.ComponentException;
import com.adyen.checkout.core.log.LogUtil;
import com.adyen.checkout.core.log.Logger;
import com.adyen.checkout.core.util.StringUtil;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class RedirectComponent extends BaseActionComponent {
    private static final String TAG = LogUtil.getTag();

    public static final ActionComponentProvider<RedirectComponent> PROVIDER =
            new ActionComponentProviderImpl<>(RedirectComponent.class);

    public RedirectComponent(@NonNull Application application) {
        super(application);
    }

    /**
     * Returns the suggested value to be used as the `returnUrl` value in the payments/ call.
     *
     * @param context The context provides the package name which constitutes part of the ReturnUrl
     * @return The suggested `returnUrl` to be used. Consists of {@link RedirectUtil#REDIRECT_RESULT_SCHEME} + App package name.
     */
    @NonNull
    public static String getReturnUrl(@NonNull Context context) {
        return RedirectUtil.REDIRECT_RESULT_SCHEME + context.getPackageName();
    }

    /**
     * Make a redirect from the provided Activity to the target of the Redirect object.
     *
     * @param activity The Activity starting the redirect.
     * @param redirectAction The object from the server response defining where to redirect to.
     */
    public static void makeRedirect(@NonNull Activity activity, @NonNull RedirectAction redirectAction) throws ComponentException {
        Logger.d(TAG, "makeRedirect - " + redirectAction.getUrl());
        if (StringUtil.hasContent(redirectAction.getUrl())) {
            final Uri redirectUri = Uri.parse(redirectAction.getUrl());
            final Intent redirectIntent = RedirectUtil.createRedirectIntent(activity, redirectUri);
            activity.startActivity(redirectIntent);
        } else {
            throw new ComponentException("Redirect URL is empty.");
        }
    }

    @Override
    @NonNull
    protected List<String> getSupportedActionTypes() {
        final String[] supportedCodes = {RedirectAction.ACTION_TYPE};
        return Collections.unmodifiableList(Arrays.asList(supportedCodes));
    }

    @Override
    protected void handleActionInternal(@NonNull Activity activity, @NonNull Action action) throws ComponentException {
        final RedirectAction redirectAction = (RedirectAction) action;
        makeRedirect(activity, redirectAction);
    }

    /**
     * Call this method when receiving the return URL from the redirect with the result data.
     * This result will be in the {@link Intent#getData()} and begins with the returnUrl you specified on the payments/ call.
     *
     * @param data The Uri from the response.
     */
    public void handleRedirectResponse(@NonNull Uri data) {
        try {
            final JSONObject parsedResult = RedirectUtil.parseRedirectResult(data);
            notifyDetails(parsedResult);
        } catch (CheckoutException e) {
            notifyException(e);
        }
    }


}
