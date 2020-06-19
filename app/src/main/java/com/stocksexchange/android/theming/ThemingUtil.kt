package com.stocksexchange.android.theming

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.theming.model.*
import com.stocksexchange.android.ui.currencymarketpreview.views.chartviews.DepthChartView
import com.stocksexchange.android.ui.currencymarketpreview.views.chartviews.PriceChartView
import com.stocksexchange.android.ui.currencymarketpreview.views.orderbook.OrderbookView
import com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory.TradeHistoryView
import com.stocksexchange.android.ui.login.views.LoginCredentialsView
import com.stocksexchange.android.ui.login.views.confirmationviews.base.BaseLoginConfirmationView
import com.stocksexchange.android.ui.orderbook.views.OrderbookDetailsView
import com.stocksexchange.android.ui.passwordrecovery.views.PasswordChangeCredentialsView
import com.stocksexchange.android.ui.passwordrecovery.views.PasswordRecoveryEmailCredentialView
import com.stocksexchange.android.ui.registration.views.RegistrationCredentialsView
import com.stocksexchange.android.ui.trade.views.TradeFormView
import com.stocksexchange.android.ui.transactioncreation.depositcreation.views.DepositCreationView
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.views.WithdrawalCreationView
import com.stocksexchange.android.ui.views.*
import com.stocksexchange.android.ui.views.detailsviews.BaseDetailsView
import com.stocksexchange.android.ui.views.dialogs.DeviceMetricsDialog
import com.stocksexchange.android.ui.views.dialogs.FingerprintDialog
import com.stocksexchange.android.ui.views.dialogs.QrDialog
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.android.ui.views.mapviews.SpaceMapView
import com.stocksexchange.android.ui.views.mapviews.VerticalMapView
import com.stocksexchange.android.ui.views.popupmenu.PopupMenu
import com.stocksexchange.android.ui.views.toolbars.SearchToolbar
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.core.Constants
import com.stocksexchange.core.utils.extensions.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * A utility class used for theming different views and widgets.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
object ThemingUtil {


    object Main {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            toolbar(toolbar, theme.generalTheme)
        }


        fun toolbar(toolbar: Toolbar, theme: GeneralTheme) {
            with(toolbar) {
                setBackgroundColor(theme.primaryColor)
                setButtonDrawableColor(theme.primaryTextColor)
                setTitleColor(theme.primaryTextColor)
                setProgressBarColor(theme.progressBarColor)
            }
        }


        fun toolbarBackground(view: View, theme: Theme) {
            toolbarBackground(view, theme.generalTheme)
        }


        fun toolbarBackground(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.primaryColor)
        }


        fun toolbarTitle(textView: TextView, theme: Theme) {
            toolbarTitle(textView, theme.generalTheme)
        }


        fun toolbarTitle(textView: TextView, theme: GeneralTheme) {
            textView.setTextColor(theme.primaryTextColor)
        }


        fun toolbarIcon(imageView: ImageView, theme: Theme) {
            toolbarIcon(imageView, theme.generalTheme)
        }


        fun toolbarIcon(imageView: ImageView, theme: GeneralTheme) {
            imageView.setColor(theme.primaryTextColor)
        }


        fun searchToolbar(searchToolbar: SearchToolbar, theme: Theme) {
            searchToolbar(searchToolbar, theme.generalTheme)
        }


        fun searchToolbar(searchToolbar: SearchToolbar, theme: GeneralTheme) {
            with(searchToolbar) {
                setBackgroundColor(theme.primaryColor)
                setButtonDrawableColor(theme.primaryTextColor)
                setHintColor(theme.primaryTextColor)
                setTextColor(theme.primaryTextColor)
                setProgressBarColor(theme.progressBarColor)
                setCursorDrawable(context.getCursorDrawable(theme.primaryTextColor))
            }
        }


        fun contentContainer(view: View, theme: Theme) {
            contentContainer(view, theme.generalTheme)
        }


        fun contentContainer(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.contentContainerColor)
        }


        fun lightContentContainer(view: View, theme: Theme) {
            lightContentContainer(view, theme.generalTheme)
        }


        fun lightContentContainer(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.contentContainerLightColor)
        }


    }


    object Text {


        fun primary(textView: TextView, theme: Theme) {
            primary(textView, theme.generalTheme)
        }


        fun primary(textView: TextView, theme: GeneralTheme) {
            textView.setTextColor(theme.primaryTextColor)
        }


        fun primaryDark(textView: TextView, theme: Theme) {
            primaryDark(textView, theme.generalTheme)
        }


        fun primaryDark(textView: TextView, theme: GeneralTheme) {
            textView.setTextColor(theme.primaryDarkTextColor)
        }


        fun secondary(textView: TextView, theme: Theme) {
            secondary(textView, theme.generalTheme)
        }


        fun secondary(textView: TextView, theme: GeneralTheme) {
            textView.setTextColor(theme.secondaryTextColor)
        }


        fun secondaryDark(textView: TextView, theme: Theme) {
            secondaryDark(textView, theme.generalTheme)
        }


        fun secondaryDark(textView: TextView, theme: GeneralTheme) {
            textView.setTextColor(theme.secondaryDarkTextColor)
        }


        fun accent(textView: TextView, theme: Theme) {
            accent(textView, theme.generalTheme)
        }


        fun accent(textView: TextView, theme: GeneralTheme) {
            textView.setTextColor(theme.accentColor)
        }


    }


    object Buttons {


        fun primaryButton(button: Button, theme: Theme) {
            primaryButton(button, theme.buttonTheme)
        }


        fun primaryButton(button: Button, theme: ButtonTheme) {
            button.toPrimaryButton(
                theme.contentColor,
                theme.pressedStateBackgroundColor,
                theme.releasedStateBackgroundColor
            )
        }


        fun floatingActionButton(floatingActionButton: FloatingActionButton, theme: Theme) {
            floatingActionButton(floatingActionButton, theme.generalTheme)
        }


        fun floatingActionButton(floatingActionButton: FloatingActionButton, theme: GeneralTheme) {
            with(floatingActionButton) {
                backgroundTintList = ColorStateList.valueOf(theme.accentColor)
                setColor(theme.primaryTextColor)
            }
        }


    }


    object CardItem {


        fun cardView(cardView: CardView, theme: Theme) {
            cardView(cardView, theme.cardViewTheme)
        }


        fun cardView(cardView: CardView, theme: CardViewTheme) {
            cardView.setCardBackgroundColor(theme.backgroundColor)
        }


        fun primaryText(textView: TextView, theme: Theme) {
            primaryText(textView, theme.cardViewTheme)
        }


        fun primaryText(textView: TextView, theme: CardViewTheme) {
            textView.setTextColor(theme.primaryTextColor)
        }


        fun primaryDarkText(textView: TextView, theme: Theme) {
            primaryDarkText(textView, theme.cardViewTheme)
        }


        fun primaryDarkText(textView: TextView, theme: CardViewTheme) {
            textView.setTextColor(theme.primaryDarkTextColor)
        }


        fun icon(imageView: ImageView, theme: Theme) {
            icon(imageView, theme.cardViewTheme)
        }


        fun icon(imageView: ImageView, theme: CardViewTheme) {
            imageView.setColor(theme.primaryTextColor)
        }


        fun positiveButton(textView: TextView, theme: Theme) {
            positiveButton(textView, theme.cardViewTheme)
        }


        fun positiveButton(textView: TextView, theme: CardViewTheme) {
            textView.toSecondaryButton(
                textView.getColor(R.color.colorGreenAccent),
                theme.backgroundColor
            )
        }


        fun negativeButton(textView: TextView, theme: Theme) {
            negativeButton(textView, theme.cardViewTheme)
        }


        fun negativeButton(textView: TextView, theme: CardViewTheme) {
            textView.toSecondaryButton(
                textView.getColor(R.color.colorRedAccent),
                theme.backgroundColor
            )
        }


        fun neutralButton(textView: TextView, theme: Theme) {
            neutralButton(textView, theme.cardViewTheme)
        }


        fun neutralButton(textView: TextView, theme: CardViewTheme) {
            textView.toSecondaryButton(
                textView.getColor(R.color.colorBlueAccent),
                theme.backgroundColor
            )
        }


        fun normalButton(textView: TextView, theme: Theme) {
            normalButton(textView, theme.cardViewTheme)
        }


        fun normalButton(textView: TextView, theme: CardViewTheme) {
            with(textView) {
                background = context.getRoundedButtonBackgroundDrawable(
                    disabledStateBackgroundColor = theme.buttonDisabledBackgroundColor,
                    enabledStateBackgroundColor = theme.buttonEnabledBackgroundColor
                )

                setTextColor(context.getCardViewButtonTextSelector(
                    disabledStateTextColor = theme.buttonDisabledTextColor,
                    enabledStateTextColor = theme.buttonEnabledTextColor
                ))
            }
        }


        fun editText(editText: EditText, theme: Theme) {
            editText(editText, theme.cardViewTheme)
        }


        fun editText(editText: EditText, theme: CardViewTheme) {
            with(editText) {
                setHintTextColor(theme.primaryTextColor)
                setTextColor(theme.primaryTextColor)
                setCursorDrawable(context.getCursorDrawable(theme.primaryTextColor))
            }
        }


    }


    object Dialogs {


        fun dialogBuilder(materialDialogBuilder: MaterialDialog.Builder, theme: Theme) {
            dialogBuilder(materialDialogBuilder, theme.dialogTheme)
        }


        fun dialogBuilder(materialDialogBuilder: MaterialDialog.Builder, theme: DialogTheme) {
            with(materialDialogBuilder) {
                titleColor(theme.titleColor)
                contentColor(theme.textColor)
                itemsColor(theme.textColor)
                choiceWidgetColor(ColorStateList.valueOf(theme.widgetColor))
                positiveColor(theme.buttonColor)
                negativeColor(theme.buttonColor)
                backgroundColor(theme.backgroundColor)
            }
        }


        fun deviceMetricsDialog(dialog: DeviceMetricsDialog, theme: Theme) {
            with(dialog) {
                setBackgroundColor(theme.dialogTheme.backgroundColor)
                setTitlesColor(theme.dialogTheme.titleColor)
                setValuesColor(theme.generalTheme.accentColor)
            }
        }


        fun qrDialog(qrDialog: QrDialog, theme: Theme) {
            with(qrDialog) {
                setBackgroundColor(theme.dialogTheme.backgroundColor)
                setTitleTextColor(theme.dialogTheme.titleColor)
                setQrCodeTextColor(theme.dialogTheme.textColor)
                setButtonsBackgroundColor(
                    theme.generalTheme.darkAccentColor,
                    theme.generalTheme.accentColor
                )
            }
        }


        fun fingerprintDialog(dialog: FingerprintDialog, theme: Theme) {
            with(dialog) {
                setBackgroundColor(theme.dialogTheme.backgroundColor)
                setTitleTextColor(theme.dialogTheme.textColor)
                setSubtitleTextColor(theme.dialogTheme.textColor)
                setDescriptionTextColor(theme.dialogTheme.textColor)
                setStatusTextColor(theme.generalTheme.secondaryTextColor)
                setButtonTextColor(theme.generalTheme.accentColor)
            }
        }


    }


    object DottedMap {


        fun title(textView: TextView, theme: Theme) {
            title(textView, theme.dottedMapViewTheme)
        }


        fun title(textView: TextView, theme: DottedMapViewTheme) {
            textView.setTextColor(theme.titleColor)
        }


        fun view(view: DottedMapView, theme: Theme) {
            view(view, theme.dottedMapViewTheme)
        }


        fun view(view: DottedMapView, theme: DottedMapViewTheme) {
            with(view) {
                setTitleColor(theme.titleColor)
                setSeparatorColor(theme.titleColor)
                setValueColor(theme.textColor)
            }
        }


    }


    object TabBar {


        fun tabLayout(tabLayout: TabLayout, theme: Theme) {
            tabLayout(tabLayout, theme.generalTheme)
        }


        fun tabLayout(tabLayout: TabLayout, theme: GeneralTheme) {
            with(tabLayout) {
                setBackgroundColor(theme.primaryColor)
                setTabTextColors(theme.secondaryTextColor, theme.primaryTextColor)
                setSelectedTabIndicatorColor(theme.tabIndicatorColor)
            }
        }


        fun tabsBackground(view: View, theme: Theme) {
            tabsBackground(view, theme.generalTheme)
        }


        fun tabsBackground(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.primaryColor)
        }


        fun tabText(view: View, theme: Theme) {
            tabText(view, theme.generalTheme)
        }


        fun tabText(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.primaryTextColor)
        }


        fun tabIndicator(view: View, theme: Theme) {
            tabIndicator(view, theme.generalTheme)
        }


        fun tabIndicator(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.tabIndicatorColor)
        }


    }


    object SortPanel {


        fun background(view: View, theme: Theme) {
            background(view, theme.sortPanelTheme)
        }


        fun background(view: View, theme: SortPanelTheme) {
            view.setBackgroundColor(theme.backgroundColor)
        }


        fun selectedTitle(textView: TextView, theme: Theme) {
            selectedTitle(textView, theme.sortPanelTheme)
        }


        fun selectedTitle(textView: TextView, theme: SortPanelTheme) {
            textView.setTextColor(theme.selectedTitleColor)
        }


        fun unselectedTitle(textView: TextView, theme: Theme) {
            unselectedTitle(textView, theme.sortPanelTheme)
        }


        fun unselectedTitle(textView: TextView, theme: SortPanelTheme) {
            textView.setTextColor(theme.unselectedTitleColor)
        }


    }


    object Popup {


        fun menu(popupMenu: PopupMenu, theme: Theme) {
            menu(popupMenu, theme.popupMenuTheme)
        }


        fun menu(popupMenu: PopupMenu, theme: PopupMenuTheme) {
            popupMenu.setBackgroundColor(theme.backgroundColor)
        }


        object Item {


            fun title(textView: TextView, theme: Theme) {
                title(textView, theme.popupMenuTheme)
            }


            fun title(textView: TextView, theme: PopupMenuTheme) {
                textView.setTextColor(theme.titleColor)
            }


            fun separator(imageView: ImageView, theme: Theme) {
                separator(imageView, theme.popupMenuTheme)
            }


            fun separator(imageView: ImageView, theme: PopupMenuTheme) {
                imageView.setImageDrawable(imageView.context.getDottedLineDrawable(theme.titleColor))
            }


        }


    }


    object Splash {


        fun infoViewTitle(textView: TextView) {
            with(textView) {
                setTextColor(getColor(R.color.deepTealPrimaryTextColor))
            }
        }


        fun infoViewProgressBar(progressBar: ProgressBar) {
            with(progressBar) {
                setColor(getColor(R.color.deepTealProgressBarColor))
            }
        }


    }


    object UserAdmission {


        fun contentContainer(contentContainerLl: View, theme: Theme) {
            contentContainer(contentContainerLl, theme.generalTheme)
        }


        fun contentContainer(contentContainerLl: View, theme: GeneralTheme) {
            contentContainerLl.setBackgroundColor(theme.primaryColor)
        }


        fun title(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun title(imageView: ImageView, theme: Theme) {
            if (theme.name == com.stocksexchange.android.theming.model.Themes.DEEP_TEAL.title) {
                imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_stex_logo))
            }
        }


        fun motto(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun infoView(infoView: InfoView, theme: Theme) {
            infoView(infoView, theme.generalTheme)
        }


        fun infoView(infoView: InfoView, theme: GeneralTheme) {
            infoView.setColor(theme.primaryTextColor)
        }


        fun infoView(infoView: InfoView, color: Int) {
            infoView.setColor(color)
        }


        fun progressBar(progressBar: ProgressBar, theme: Theme) {
            progressBar(progressBar, theme.generalTheme)
        }


        fun progressBar(progressBar: ProgressBar, theme: GeneralTheme) {
            progressBar.setColor(theme.primaryTextColor)
        }


        fun button(button: UserAdmissionButton, theme: Theme) {
            button(button, theme.generalTheme)
        }


        fun button(button: UserAdmissionButton, theme: GeneralTheme) {
            with(button) {
                setButtonTextColor(theme.primaryTextColor)
                setButtonProgressBarColor(theme.primaryTextColor)
                setButtonBackgroundColor(theme.accentColor)
            }
        }


        fun secondaryButton(button: UserAdmissionButton, theme: Theme) {
            secondaryButton(button, theme.generalTheme)
        }


        fun secondaryButton(button: UserAdmissionButton, theme: GeneralTheme) {
            with(button) {
                setButtonTextColor(theme.secondaryTextColor)
                setButtonProgressBarColor(theme.secondaryTextColor)
                setButtonBackground(R.drawable.bordered_button_background_login)
            }
        }


    }


    object Login {


        fun credentialsView(credentialsView: LoginCredentialsView, theme: Theme) {
            credentialsView(credentialsView, theme.generalTheme)
        }


        fun credentialsView(credentialsView: LoginCredentialsView, theme: GeneralTheme) {
            with(credentialsView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)
                val inputViewIconColor = getColor(R.color.colorInputViewIconColor)
                val transparentColor = Color.TRANSPARENT

                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintColor)
                setInputViewCursorColor(theme.primaryTextColor)
                setInputViewIconColor(inputViewIconColor)
                setInputViewExtraViewContainerBackgroundColor(transparentColor)
                setForgotPasswordTextColor(theme.accentColor)
                setInputLabelsColor(theme.secondaryTextColor)
                setDoNotHaveAccountTextColor(theme.secondaryTextColor)
            }
        }


        fun accountVerificationView(infoView: InfoView, theme: Theme) {
            UserAdmission.infoView(infoView, theme.generalTheme.secondaryTextColor)
        }


        fun confirmationView(confirmationView: BaseLoginConfirmationView, theme: Theme) {
            confirmationView(confirmationView, theme.generalTheme)
            confirmationView.setTextColor(theme.generalTheme.secondaryTextColor)
        }


        fun confirmationView(confirmationView: BaseLoginConfirmationView, theme: GeneralTheme) {
            with(confirmationView) {
                val inputViewHintTextColor = getColor(R.color.colorInputViewHintColor)

                setHintTextColor(theme.primaryTextColor)
                setHelpButtonTextColor(theme.primaryTextColor)
                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintTextColor)
                setInputViewCursorColor(theme.primaryTextColor)
            }
        }


    }


    object Registration {


        fun credentialsView(credentialsView: RegistrationCredentialsView, theme: Theme) {
            credentialsView(credentialsView, theme.generalTheme)
        }


        fun credentialsView(credentialsView: RegistrationCredentialsView, theme: GeneralTheme) {
            with(credentialsView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)
                val inputViewIconColor = getColor(R.color.colorInputViewIconColor)
                val transparentColor = Color.TRANSPARENT

                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintColor)
                setInputViewCursorColor(theme.primaryTextColor)
                setInputViewIconColor(inputViewIconColor)
                setInputViewExtraViewContainerBackgroundColor(transparentColor)
                setCheckBoxColor(theme.accentColor)
                setCheckBoxTextColor(theme.primaryTextColor)
                setCheckBoxTextHighlightColor(transparentColor)
                setInputLabelColor(theme.secondaryTextColor)
            }
        }


        fun accountVerificationView(infoView: InfoView, theme: Theme) {
            UserAdmission.infoView(infoView, theme)
        }


        fun textView(textView: TextView, theme: Theme) {
            textView.setTextColor(theme.generalTheme.secondaryTextColor)
        }


    }


    object PasswordRecovery {


        fun credentialsView(credentialsView: PasswordChangeCredentialsView, theme: Theme) {
            credentialsView(credentialsView, theme.generalTheme)
        }


        fun credentialsView(credentialsView: PasswordChangeCredentialsView, theme: GeneralTheme) {
            with(credentialsView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)
                val inputViewIconColor = getColor(R.color.colorInputViewIconColor)

                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintColor)
                setInputViewCursorColor(theme.primaryTextColor)
                setInputViewIconColor(inputViewIconColor)
                setInputLabelTextColor(theme.secondaryTextColor)
            }
        }


        fun emailCredentialView(emailCredentialView: PasswordRecoveryEmailCredentialView, theme: Theme) {
            emailCredentialView(emailCredentialView, theme.generalTheme)
        }


        fun emailCredentialView(emailCredentialView: PasswordRecoveryEmailCredentialView, theme: GeneralTheme) {
            with(emailCredentialView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)

                setHintTextColor(theme.secondaryTextColor)
                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintColor)
                setInputViewCursorColor(theme.primaryTextColor)
                setEmailLabelTextColor(theme.secondaryTextColor)
            }
        }


        fun successInfoView(infoView: InfoView, theme: Theme) {
            UserAdmission.infoView(infoView, theme.generalTheme.secondaryTextColor)
        }


        fun textView(textView: TextView, theme: Theme) {
            textView.setTextColor(theme.generalTheme.secondaryTextColor)
        }


    }


    object Authentication {


        fun contentContainer(contentContainerRl: RelativeLayout, theme: Theme) {
            contentContainer(contentContainerRl, theme.generalTheme)
        }


        fun contentContainer(contentContainerRl: RelativeLayout, theme: GeneralTheme) {
            with(contentContainerRl) {
                background = context.getPortraitParticlesDrawable(
                    theme.gradientStartColor,
                    theme.gradientEndColor
                )
            }
        }


        fun appLogo(imageView: ImageView, theme: Theme) {
            appLogo(imageView, theme.generalTheme)
        }


        fun appLogo(imageView: ImageView, theme: GeneralTheme) {
            imageView.setColor(theme.primaryTextColor)
        }


        fun message(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun emptyPinBox(imageView: ImageView, theme: Theme) {
            emptyPinBox(imageView, theme.generalTheme)
        }


        fun emptyPinBox(imageView: ImageView, theme: GeneralTheme) {
            imageView.setImageDrawable(imageView.context.getPinBoxBorderDrawable(theme.accentColor))
        }


        fun filledPinBox(imageView: ImageView, theme: Theme) {
            filledPinBox(imageView, theme.generalTheme)

        }


        fun filledPinBox(imageView: ImageView, theme: GeneralTheme) {
            imageView.setImageDrawable(imageView.context.getPinBoxSolidDrawable(theme.accentColor))
        }


        fun pinEntryKeypad(pinEntryKeypad: PinEntryKeypad, theme: Theme) {
            apply(pinEntryKeypad, theme)
        }


        fun helpButton(textView: TextView, theme: Theme) {
            helpButton(textView, theme.generalTheme)
        }


        fun helpButton(textView: TextView, theme: GeneralTheme) {
            with(textView) {
                setTextColor(context.getButtonTextSelector(
                    pressedStateTextColor = theme.accentColor.adjustAlpha(0.5f),
                    releasedStateTextColor = theme.accentColor
                ))
            }
        }


        fun fingerprintDialog(dialog: FingerprintDialog, theme: Theme) {
            Dialogs.fingerprintDialog(dialog, theme)
        }


    }


    object PinRecovery {


        fun contentContainer(contentContainerRl: RelativeLayout, theme: Theme) {
            contentContainer(contentContainerRl, theme.generalTheme)
        }


        fun contentContainer(contentContainerRl: RelativeLayout, theme: GeneralTheme) {
            contentContainerRl.setBackgroundColor(theme.primaryColor)
        }


        fun appLogo(imageView: ImageView, theme: Theme) {
            appLogo(imageView, theme.generalTheme)
        }


        fun appLogo(imageView: ImageView, theme: GeneralTheme) {
            imageView.setColor(theme.primaryTextColor)
        }


        fun appMotto(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun title(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun subtitle(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun cancellationButton(button: Button, theme: Theme) {
            cancellationButton(button, theme.generalTheme)
        }


        fun cancellationButton(button: Button, theme: GeneralTheme) {
            button.toBorderedButton(theme.secondaryTextColor)
        }


        fun confirmationButton(button: Button, theme: Theme) {
            confirmationButton(button, theme.generalTheme)
        }


        fun confirmationButton(button: Button, theme: GeneralTheme) {
            button.toNewPrimaryButton(
                textColor = theme.primaryTextColor,
                backgroundColor = theme.accentColor
            )
        }


    }


    object Dashboard {


        fun bottomNavigationView(bottomNavigationView: BottomNavigationView, theme: Theme) {
            bottomNavigationView(bottomNavigationView, theme.generalTheme)
        }


        fun bottomNavigationView(bottomNavigationView: BottomNavigationView, theme: GeneralTheme) {
            with(bottomNavigationView) {
                setBackgroundColor(theme.dashboardBottomNavBackgroundColor)
                setItemColors(
                    unselectedStateColor = theme.secondaryTextColor,
                    selectedStateColor = theme.accentColor
                )
            }
        }
    }


    object VerificationPrompt {


        fun contentContainer(view: View, theme: Theme) {
            contentContainer(view, theme.generalTheme)
        }


        fun contentContainer(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(if(Constants.API_VERSION != Build.VERSION_CODES.O) {
                theme.primaryColor.adjustAlpha(0.9f)
            } else {
                theme.primaryColor
            })
        }


        fun title(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun description(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun verifyNowButton(button: Button, theme: Theme) {
            verifyNowButton(button, theme.generalTheme)
        }


        fun verifyNowButton(button: Button, theme: GeneralTheme) {
            button.toNewPrimaryButton(
                textColor = theme.primaryTextColor,
                backgroundColor = theme.accentColor
            )
        }


        fun verifyLaterButton(button: Button, theme: Theme) {
            verifyLaterButton(button, theme.generalTheme)
        }


        fun verifyLaterButton(button: Button, theme: GeneralTheme) {
            button.toBorderedButton(theme.secondaryTextColor)
        }


    }


    object Wallets {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun popupMenu(popupMenu: PopupMenu, theme: Theme) {
            apply(popupMenu, theme)
        }


        object WalletItem {


            fun cardView(cardView: CardView, theme: Theme) {
                CardItem.cardView(cardView, theme)
            }


            fun currencyName(textView: TextView, theme: Theme) {
                currencyName(textView, theme.cardViewTheme)
            }


            fun currencyName(textView: TextView, theme: CardViewTheme) {
                textView.toSecondaryButton(
                    textView.getColor(R.color.colorYellowAccent),
                    theme.backgroundColor
                )
            }


            fun currencyLongName(textView: TextView, theme: Theme) {
                DottedMap.title(textView, theme)
            }


            fun dottedMap(dottedMapView: DottedMapView, theme: Theme) {
                DottedMap.view(dottedMapView, theme)
            }


            fun depositButton(textView: TextView, theme: Theme) {
                depositButton(textView, theme.cardViewTheme)
            }


            fun depositButton(textView: TextView, theme: CardViewTheme) {
                with(textView) {
                    background = context.getRoundedButtonBackgroundDrawable(
                        disabledStateBackgroundColor = theme.buttonDisabledBackgroundColor,
                        enabledStateBackgroundColor = theme.buttonEnabledBackgroundColor
                    )

                    setTextColor(context.getCardViewButtonTextSelector(
                        disabledStateTextColor = theme.buttonDisabledTextColor,
                        enabledStateTextColor = theme.buttonEnabledTextColor
                    ))
                }
            }


            fun withdrawButton(textView: TextView, theme: Theme) {
                withdrawButton(textView, theme.cardViewTheme)
            }


            fun withdrawButton(textView: TextView, theme: CardViewTheme) {
                with(textView) {
                    background = context.getRoundedButtonBackgroundDrawable(
                        disabledStateBackgroundColor = theme.buttonDisabledBackgroundColor,
                        enabledStateBackgroundColor = theme.buttonEnabledBackgroundColor
                    )

                    setTextColor(context.getCardViewButtonTextSelector(
                        disabledStateTextColor = theme.buttonDisabledTextColor,
                        enabledStateTextColor = theme.buttonEnabledTextColor
                    ))
                }
            }


        }


    }


    object ProtocolSelection {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        object Item {


            fun cardView(cardView: CardView, theme: Theme) {
                cardView(cardView, theme.generalTheme)
            }


            fun cardView(cardView: CardView, theme: GeneralTheme) {
                cardView.setCardBackgroundColor(theme.primaryColor)
            }


            fun name(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


        }


    }


    object TransactionCreation {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun contentContainer(view: View, theme: Theme) {
            contentContainer(view, theme.generalTheme)
        }


        fun contentContainer(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.primaryColor)
        }


    }


    object DepositCreation {


        fun currencySymbol(currencySymbol: TextView, theme: Theme) {
            currencySymbol(currencySymbol, theme.generalTheme)
        }


        fun currencySymbol(currencySymbol: TextView, theme: GeneralTheme) {
            currencySymbol.toSecondaryButton(
                currencySymbol.getColor(R.color.colorYellowAccent),
                theme.primaryColor
            )
        }


        fun depositCreationView(depositCreationView: DepositCreationView, theme: Theme) {
            depositCreationView(depositCreationView, theme.generalTheme)
        }


        fun depositCreationView(depositCreationView: DepositCreationView, theme: GeneralTheme) {
            with(depositCreationView) {
                val yellowAccentColor = getColor(R.color.colorYellowAccent)

                setParameterValueTextColor(theme.accentColor)
                setParameterValuePressedBackgroundColor(theme.accentColor.adjustAlpha(0.5f))
                setExtraParamTitleColor(yellowAccentColor)
                setSimpleMapViewTitleColor(theme.secondaryTextColor)
                setSimpleMapViewValueColor(theme.primaryTextColor)
            }
        }


        fun warning(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun primaryButton(button: Button, theme: Theme) {
            primaryButton(button, theme.generalTheme)
        }


        fun primaryButton(button: Button, theme: GeneralTheme) {
            button.toNewPrimaryButton(
                textColor = theme.primaryTextColor,
                backgroundColor = theme.accentColor
            )
        }


        fun secondaryButton(button: Button, theme: Theme) {
            secondaryButton(button, theme.generalTheme)
        }


        fun secondaryButton(button: Button, theme: GeneralTheme) {
            button.toBorderedButton(theme.secondaryTextColor)
        }


    }


    object WithdrawalCreation {


        fun availableBalanceTitle(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun availableBalanceValue(textView: TextView, theme: Theme) {
            availableBalanceValue(textView, theme.generalTheme)
        }


        fun availableBalanceValue(textView: TextView, theme: GeneralTheme) {
            textView.toSecondaryButton(
                textView.getColor(R.color.colorYellowAccent),
                theme.primaryColor
            )
        }


        fun withdrawalCreationView(withdrawalCreationView: WithdrawalCreationView, theme: Theme) {
            withdrawalCreationView(withdrawalCreationView, theme.generalTheme)
        }


        fun withdrawalCreationView(withdrawalCreationView: WithdrawalCreationView, theme: GeneralTheme) {
            with(withdrawalCreationView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)

                setLabelTextColor(theme.secondaryTextColor)
                setSimpleMapViewTitleColor(theme.secondaryTextColor)
                setSimpleMapViewValueColor(theme.primaryTextColor)
                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintColor)
                setInputViewCursorColor(theme.primaryTextColor)
                setInputViewIconColor(theme.primaryTextColor)
                setInputViewLabelTextColor(theme.primaryTextColor)
                setInputViewExtraViewContainerBackgroundColor(theme.accentColor)
            }
        }


        fun withdrawButton(button: Button, theme: Theme) {
            withdrawButton(button, theme.generalTheme)
        }


        fun withdrawButton(button: Button, theme: GeneralTheme) {
            button.toNewPrimaryButton(
                textColor = theme.primaryTextColor,
                backgroundColor = theme.accentColor
            )
        }


    }


    object QrCodeScanner {


        fun backButton(imageView: ImageView, theme: Theme) {
            Main.toolbarIcon(imageView, theme)
        }


        fun scannerView(scannerView: ZXingScannerView, theme: Theme) {
            scannerView(scannerView, theme.generalTheme)
        }


        fun scannerView(scannerView: ZXingScannerView, theme: GeneralTheme) {
            with(scannerView) {
                setBorderColor(theme.accentColor)
            }
        }


        fun hint(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


    }


    object Transactions {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        object TransactionItem {


            fun cardView(cardView: CardView, theme: Theme) {
                CardItem.cardView(cardView, theme)
            }


            fun title(textView: TextView, theme: Theme) {
                DottedMap.title(textView, theme)
            }


            fun dottedMap(dottedMapView: DottedMapView, theme: Theme) {
                DottedMap.view(dottedMapView, theme)
            }


            fun statusButton(textView: TextView, theme: Theme,
                             backgroundColor: Int) {
                statusButton(textView, theme.cardViewTheme, backgroundColor)
            }


            fun statusButton(textView: TextView, theme: CardViewTheme,
                             backgroundColor: Int) {
                textView.toSecondaryButton(
                    backgroundColor = backgroundColor,
                    foregroundColor = theme.backgroundColor
                )
            }


            fun leftActionButton(textView: TextView, theme: Theme) {
                leftActionButton(textView, theme.cardViewTheme)
            }


            fun leftActionButton(textView: TextView, theme: CardViewTheme) {
                with(textView) {
                    background = context.getRoundedButtonBackgroundDrawable(
                        disabledStateBackgroundColor = theme.buttonDisabledBackgroundColor,
                        enabledStateBackgroundColor = theme.buttonEnabledBackgroundColor
                    )

                    setTextColor(context.getCardViewButtonTextSelector(
                        disabledStateTextColor = theme.buttonDisabledTextColor,
                        enabledStateTextColor = theme.buttonEnabledTextColor
                    ))
                }
            }


            fun rightActionButton(textView: TextView, theme: Theme) {
                rightActionButton(textView, theme.cardViewTheme)
            }


            fun rightActionButton(textView: TextView, theme: CardViewTheme) {
                with(textView) {
                    background = context.getRoundedButtonBackgroundDrawable(
                        disabledStateBackgroundColor = theme.buttonDisabledBackgroundColor,
                        enabledStateBackgroundColor = theme.buttonEnabledBackgroundColor
                    )

                    setTextColor(context.getCardViewButtonTextSelector(
                        disabledStateTextColor = theme.buttonDisabledTextColor,
                        enabledStateTextColor = theme.buttonEnabledTextColor
                    ))
                }
            }


        }


    }


    object Orders {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        object OrderItem {


            fun cardView(cardView: CardView, theme: Theme) {
                CardItem.cardView(cardView, theme)
            }


            fun buyButton(textView: TextView, theme: Theme) {
                CardItem.positiveButton(textView, theme)
            }


            fun sellButton(textView: TextView, theme: Theme) {
                CardItem.negativeButton(textView, theme)
            }


            fun unknownTypeButton(textView: TextView, theme: Theme) {
                CardItem.neutralButton(textView, theme)
            }


            fun marketName(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


            fun dottedMap(dottedMapView: DottedMapView, theme: Theme) {
                DottedMap.view(dottedMapView, theme)
            }


            fun cancelButton(textView: TextView, theme: Theme) {
                CardItem.normalButton(textView, theme)
            }


        }


    }


    object CurrencyMarketsContainer {


        fun sortPanel(sortPanel: CurrencyMarketsSortPanel, theme: Theme) {
            apply(sortPanel, theme)
        }


    }


    object CurrencyMarkets {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        object Item {


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.generalTheme)
            }


            fun contentContainer(view: View, theme: GeneralTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.primaryColor)
                }
            }


            fun baseCurrencySymbol(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


            fun separator(textView: TextView, theme: Theme) {
                Text.secondaryDark(textView, theme)
            }


            fun quoteCurrencySymbol(textView: TextView, theme: Theme) {
                Text.secondaryDark(textView, theme)
            }


            fun dailyVolume(textView: TextView, theme: Theme) {
                Text.secondaryDark(textView, theme)
            }


            fun lastPrice(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


            fun lastPriceInFiatCurrency(textView: TextView, theme: Theme) {
                Text.secondaryDark(textView, theme)
            }


            fun positiveDailyPriceChange(textView: TextView) {
                with(textView) {
                    setTextColor(getColor(R.color.colorGreenAccent))
                }
            }


            fun negativeDailyPriceChange(textView: TextView) {
                with(textView) {
                    setTextColor(getColor(R.color.colorRedAccent))
                }
            }


            fun neutralDailyPriceChange(textView: TextView) {
                with(textView) {
                    setTextColor(getColor(R.color.colorBlueAccent))
                }
            }


        }


    }


    object CurrencyMarketsSearch {


        fun sortPanel(sortPanel: CurrencyMarketsSortPanel, theme: Theme) {
            apply(sortPanel, theme)
        }


    }


    object News {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        object NewsItem {


            fun dateView(textView: TextView, theme: CardViewTheme) {
                textView.toSecondaryButton(
                    theme.primaryTextColor,
                    theme.backgroundColor
                )
            }


            fun cardView(cardView: CardView, theme: Theme) {
                CardItem.cardView(cardView, theme)
            }


            fun titleView(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.secondaryTextColor)
                textView.setLinkTextColor(theme.generalTheme.accentColor)
            }


            fun dateView(textView: TextView, theme: Theme) {
                dateView(textView, theme.cardViewTheme)
            }


            fun imageView(imageView: ImageView, theme: Theme) {
                imageView.setBackgroundColor(theme.cardViewTheme.backgroundColor)
            }


        }


    }


    object Profile {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        object Item {


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.generalTheme)
            }


            fun contentContainer(view: View, theme: GeneralTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.primaryColor)
                }
            }


            fun loginTitle(textView: TextView, theme: Theme) {
                Text.accent(textView, theme)
            }


            fun title(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


            fun description(textView: TextView, theme: Theme) {
                Text.secondary(textView, theme)
            }


            fun icon(imageView: ImageView, theme: Theme) {
                icon(imageView, theme.generalTheme)
            }


            fun icon(imageView: ImageView, theme: GeneralTheme) {
                imageView.setColor(theme.primaryTextColor)
            }


        }


    }


    object AlertPrice {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun swipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, theme: Theme) {
            apply(swipeRefreshLayout, theme)
        }


        object AlertPriceItem {


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.cardViewTheme)
            }


            fun contentContainer(view: View, theme: CardViewTheme) {
                view.setBackgroundColor(theme.backgroundColor)
            }


            fun setTitleTextColor(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.secondaryTextColor)
            }


            fun setPriceTextColor(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.secondaryTextColor)
            }


            fun setTitleTextColor(textView: TextView, color: Int) {
                textView.setTextColor(color)
            }


            fun baseCurrencySymbol(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


        }
    }


    object AlertPriceAdd {


        fun contentContainer(view: View, theme: Theme) {
            Main.toolbarBackground(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun setTitleTextColor(textView: TextView, theme: Theme) {
            textView.setTextColor(theme.generalTheme.secondaryTextColor)
        }


    }


    object Inbox {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun swipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, theme: Theme) {
            apply(swipeRefreshLayout, theme)
        }


        object InboxItem {


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.cardViewTheme)
            }


            fun contentContainer(view: View, theme: CardViewTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.backgroundColor)
                }
            }


            fun title(textView: TextView, theme: Theme) {
                DottedMap.title(textView, theme)
            }


            fun unreadTitleField(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.accentColor)
            }


            fun readTitleField(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.primaryTextColor)
            }


            fun unreadDateField(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.accentColor)
            }


            fun readDateField(textView: TextView, theme: Theme) {
                textView.setTextColor(theme.generalTheme.secondaryTextColor)
            }


            fun setColorTitleField(spaceMapView: SpaceMapView, theme: Theme) {
                spaceMapView.setTitleColor(theme.generalTheme.secondaryTextColor)
                spaceMapView.setValueColor(theme.generalTheme.primaryTextColor)
            }

            fun setGreenColorField(spaceMapView: SpaceMapView, theme: Theme) {
                spaceMapView.setTitleColor(theme.generalTheme.secondaryTextColor)
                spaceMapView.setValueColor(theme.generalTheme.accentColor)
            }


        }


    }


    object Settings {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun fingerprintDialog(fingerprintDialog: FingerprintDialog, theme: Theme) {
            Dialogs.fingerprintDialog(fingerprintDialog, theme)
        }


        fun deviceMetricsDialog(deviceMetricsDialog: DeviceMetricsDialog, theme: Theme) {
            Dialogs.deviceMetricsDialog(deviceMetricsDialog, theme)
        }


        object SettingSection {


            fun title(textView: TextView, theme: Theme) {
                title(textView, theme.generalTheme)
            }


            fun title(textView: TextView, theme: GeneralTheme) {
                textView.setTextColor(theme.accentColor)
            }


        }


        object SettingItem {


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.generalTheme)
            }


            fun contentContainer(view: View, theme: GeneralTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.primaryColor)
                }
            }



            fun title(textView: TextView, theme: Theme) {
                title(textView, theme.generalTheme)
            }


            fun title(textView: TextView, theme: GeneralTheme) {
                textView.setTextColor(theme.contentContainerTextColor)
            }


            fun description(textView: TextView, theme: Theme) {
                Text.secondary(textView, theme)
            }


            fun customDescriptionColor(textView: TextView, theme: GeneralTheme) {
                textView.setTextColor(theme.accentColor)
            }


            fun switch(switchView: SwitchCompat, theme: Theme) {
                apply(switchView, theme)
            }


        }


        object SettingAccountItem {


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.generalTheme)
            }


            fun contentContainer(view: View, theme: GeneralTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.primaryColor)
                }
            }


            fun title(textView: TextView, theme: Theme) {
                title(textView, theme.generalTheme)
            }


            fun title(textView: TextView, theme: GeneralTheme) {
                textView.setTextColor(theme.contentContainerTextColor)
            }


            fun description(textView: TextView, theme: Theme) {
                Text.secondary(textView, theme)
            }


        }


    }


    object LanguageSelection {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        object LanguageItem {


            fun title(textView: TextView, theme: Theme) {
                title(textView, theme.generalTheme)
            }


            fun title(textView: TextView, theme: GeneralTheme) {
                textView.setTextColor(theme.contentContainerTextColor)
            }


            fun contentContainer(view: View, theme: Theme) {
                contentContainer(view, theme.generalTheme)
            }


            fun contentContainer(view: View, theme: GeneralTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.primaryColor)
                }
            }


            fun selectedContentContainer(view: View, theme: Theme) {
                selectedContentContainer(view, theme.generalTheme)
            }


            fun selectedContentContainer(view: View, theme: GeneralTheme) {
                with(view) {
                    background = context.getColoredSelectableItemBackgroundDrawable(theme.accentColor)
                }
            }

        }


    }


    object Themes {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun contentContainer(view: View, theme: Theme) {
            Main.toolbarBackground(view, theme)
        }


        object Item {


            fun progressBar(progressBarIv: ImageView, theme: Theme) {
                progressBar(progressBarIv, theme.generalTheme)
            }


            fun progressBar(progressBarIv: ImageView, theme: GeneralTheme) {
                with(progressBarIv) {
                    setImageDrawable(context.getColoredCompatDrawable(
                        R.drawable.ic_progress_bar,
                        theme.accentColor
                    ))
                }
            }


        }


    }


    object VerificationSelection {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        object Item {


            fun cardView(cardView: CardView, theme: Theme) {
                cardView(cardView, theme.generalTheme)
            }


            fun cardView(cardView: CardView, theme: GeneralTheme) {
                cardView.setCardBackgroundColor(theme.primaryColor)
            }


            fun verifiedText(textView: TextView, theme: Theme) {
                verifiedText(textView, theme.generalTheme)
            }


            fun verifiedText(textView: TextView, theme: GeneralTheme) {
                textView.toSecondaryButton(
                    textView.getColor(R.color.colorGreenAccent),
                    theme.primaryColor
                )
            }


            fun feeText(textView: TextView, theme: Theme) {
                feeText(textView, theme.generalTheme)
            }


            fun feeText(textView: TextView, theme: GeneralTheme) {
                textView.toSecondaryButton(
                    textView.getColor(R.color.colorYellowAccent),
                    theme.primaryColor
                )
            }


            fun titleText(textView: TextView, theme: Theme) {
                Text.secondary(textView, theme)
            }


        }


    }


    object Referral {


        fun contentContainer(view: View, theme: Theme) {
            Main.toolbarBackground(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun title(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun subtitle(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun referralLink(textView: TextView, theme: Theme) {
            referralLink(textView, theme.generalTheme)
        }


        fun referralLink(textView: TextView, theme: GeneralTheme) {
            with(textView) {
                setTextColor(context.getButtonTextSelector(
                    pressedStateTextColor = theme.accentColor.adjustAlpha(0.5f),
                    releasedStateTextColor = theme.accentColor
                ))
            }
        }


        fun notice(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun referralInput(inputView: InputView, theme: Theme) {
            referralInput(inputView, theme.generalTheme)
        }


        fun referralInput(inputView: InputView, theme: GeneralTheme) {
            with(inputView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)

                setEtHintTextColor(inputViewHintColor)
                setEtCursorColor(theme.primaryTextColor)
                setEtTextColor(theme.primaryTextColor)
            }
        }


        fun primaryButton(button: Button, theme: Theme) {
            primaryButton(button, theme.generalTheme)
        }


        fun primaryButton(button: Button, theme: GeneralTheme) {
            button.toNewPrimaryButton(
                textColor = theme.primaryTextColor,
                backgroundColor = theme.accentColor
            )
        }


        fun secondaryButton(button: Button, theme: Theme) {
            secondaryButton(button, theme.generalTheme)
        }


        fun secondaryButton(button: Button, theme: GeneralTheme) {
            button.toBorderedButton(theme.secondaryTextColor)
        }


    }


    object Help {


        fun contentContainer(view: View, theme: Theme) {
            Main.contentContainer(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        object Item {


            fun cardView(cardView: CardView, theme: Theme) {
                cardView(cardView, theme.generalTheme)
            }


            fun cardView(cardView: CardView, theme: GeneralTheme) {
                cardView.setCardBackgroundColor(theme.primaryColor)
            }


            fun mainIcon(imageView: ImageView, theme: Theme) {
                mainIcon(imageView, theme.generalTheme)
            }


            fun mainIcon(imageView: ImageView, theme: GeneralTheme) {
                imageView.setColor(theme.secondaryTextColor)
            }


            fun arrowIcon(imageView: ImageView, theme: Theme) {
                arrowIcon(imageView, theme.generalTheme)
            }


            fun arrowIcon(imageView: ImageView, theme: GeneralTheme) {
                imageView.setColor(theme.primaryTextColor)
            }


            fun title(textView: TextView, theme: Theme) {
                Text.primary(textView, theme)
            }


            fun subtitle(textView: TextView, theme: Theme) {
                Text.secondary(textView, theme)
            }


        }


    }


    object About {


        fun contentContainer(view: View, theme: Theme) {
            Main.toolbarBackground(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun appLogo(imageView: ImageView, theme: Theme) {
            appLogo(imageView, theme.generalTheme)
        }


        fun appLogo(imageView: ImageView, theme: GeneralTheme) {
            imageView.setColor(theme.primaryTextColor)
        }


        fun appMotto(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


        fun visitWebsiteReferenceButton(referenceButtonView: ReferenceButtonView, theme: Theme) {
            visitWebsiteReferenceButton(referenceButtonView, theme.aboutTheme)
        }


        fun visitWebsiteReferenceButton(referenceButtonView: ReferenceButtonView, theme: AboutTheme) {
            referenceButton(
                referenceButtonView,
                theme.refButtonVisitWebsiteBackgroundColor,
                theme.refButtonVisitWebsiteTextColor
            )
        }


        fun termsOfUseReferenceButton(referenceButtonView: ReferenceButtonView, theme: Theme) {
            termsOfUseReferenceButton(referenceButtonView, theme.aboutTheme)
        }


        fun termsOfUseReferenceButton(referenceButtonView: ReferenceButtonView, theme: AboutTheme) {
            referenceButton(
                referenceButtonView,
                theme.refButtonTermsOfUseBackgroundColor,
                theme.refButtonTermsOfUseTextColor
            )
        }


        fun privacyPolicyReferenceButton(referenceButtonView: ReferenceButtonView, theme: Theme) {
            privacyPolicyReferenceButton(referenceButtonView, theme.aboutTheme)
        }


        fun privacyPolicyReferenceButton(referenceButtonView: ReferenceButtonView, theme: AboutTheme) {
            referenceButton(
                referenceButtonView,
                theme.refButtonPrivacyPolicyBackgroundColor,
                theme.refButtonPrivacyPolicyTextColor
            )
        }


        fun candyLinkReferenceButton(referenceButtonView: ReferenceButtonView, theme: Theme) {
            candyLinkReferenceButton(referenceButtonView, theme.aboutTheme)
        }


        fun candyLinkReferenceButton(referenceButtonView: ReferenceButtonView, theme: AboutTheme) {
            referenceButton(
                referenceButtonView,
                theme.refButtonCandyLinkBackgroundColor,
                theme.refButtonCandyLinkTextColor
            )
        }


        private fun referenceButton(referenceButtonView: ReferenceButtonView,
                                    backgroundColor: Int, textColor: Int) {
            with(referenceButtonView) {
                background = context.getReferenceButtonBackground(backgroundColor)

                setTextColor(textColor)
            }
        }


        fun facebookButton(imageView: ImageView, theme: Theme) {
            socialMediaButton(imageView, R.drawable.ic_facebook_circle, theme)
        }


        fun twitterButton(imageView: ImageView, theme: Theme) {
            socialMediaButton(imageView, R.drawable.ic_twitter_circle, theme)
        }


        fun telegramButton(imageView: ImageView, theme: Theme) {
            socialMediaButton(imageView, R.drawable.ic_telegram_circle, theme)
        }


        fun mediumButton(imageView: ImageView, theme: Theme) {
            socialMediaButton(imageView, R.drawable.ic_medium_circle, theme)
        }


        private fun socialMediaButton(imageView: ImageView, drawableId: Int,
                                      theme: Theme) {
            socialMediaButton(imageView, drawableId, theme.aboutTheme)
        }


        private fun socialMediaButton(imageView: ImageView, drawableId: Int,
                                      theme: AboutTheme) {
            with(imageView) {
                setImageDrawable(context.getColoredCompatDrawable(
                    drawableId,
                    theme.socialMediaButtonColor
                ))
            }
        }


        fun companyName(imageView: ImageView, theme: Theme) {
            companyName(imageView, theme.generalTheme)
        }


        fun companyName(imageView: ImageView, theme: GeneralTheme) {
            imageView.setImageDrawable(imageView.context.getColoredCompatDrawable(
                R.drawable.ic_company_name,
                theme.primaryTextColor
            ))
        }


        fun companyAddress(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun copyright(textView: TextView, theme: Theme) {
            Text.primary(textView, theme)
        }


    }


    object CurrencyMarketPreview {


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun swipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout, theme: Theme) {
            apply(swipeRefreshLayout, theme)
        }


        fun visibleChartsButton(imageView: ImageView, theme: Theme) {
            Main.toolbarIcon(imageView, theme)
        }


        fun hiddenChartsButton(imageView: ImageView) {
            imageView.setColor(imageView.getColor(R.color.colorGreenAccent))
        }


        fun favoriteButton(imageView: ImageView) {
            imageView.setColor(imageView.getColor(R.color.colorYellowAccent))
        }


        fun unfavoriteButton(imageView: ImageView, theme: Theme) {
            Main.toolbarIcon(imageView, theme)
        }


        fun scrollView(scrollView: LockableScrollView, theme: Theme) {
            Main.toolbarBackground(scrollView, theme)
        }


        fun headline(textView: TextView, theme: Theme) {
            headline(textView, theme.generalTheme)
        }


        fun headline(textView: TextView, theme: GeneralTheme) {
            with(textView) {
                setBackgroundColor(theme.marketPreviewHeadlineBackgroundColor)
                setTextColor(theme.primaryTextColor)
            }
        }


        fun priceInfoView(priceInfoView: CurrencyMarketPriceInfoView, theme: Theme) {
            priceInfoView(priceInfoView, theme.generalTheme)
        }


        fun priceInfoView(priceInfoView: CurrencyMarketPriceInfoView, theme: GeneralTheme) {
            with(priceInfoView) {
                priceTextColor = theme.primaryTextColor
                fiatPriceTextColor = theme.secondaryTextColor

                dailyPriceChangePositiveColor = getColor(R.color.colorGreenAccent)
                dailyPriceChangeNegativeColor= getColor(R.color.colorRedAccent)
                dailyPriceChangeNeutralColor = getColor(R.color.colorBlueAccent)
            }
        }


        fun tabLayout(tabLayout: TabLayout, theme: Theme) {
            TabBar.tabLayout(tabLayout, theme.generalTheme)
        }


        fun priceChartView(priceChartView: PriceChartView, theme: Theme) {
            apply(priceChartView, theme)
        }


        fun depthChartView(depthChartView: DepthChartView, theme: Theme) {
            apply(depthChartView, theme)
        }


        fun orderbookView(orderbookView: OrderbookView, theme: Theme) {
            apply(orderbookView, theme)
        }


        fun tradeHistoryView(tradeHistoryView: TradeHistoryView, theme: Theme) {
            apply(tradeHistoryView, theme)
        }


        fun bottomBar(view: View, theme: Theme) {
            bottomBar(view, theme.generalTheme)
        }


        fun bottomBar(view: View, theme: GeneralTheme) {
            view.setBackgroundColor(theme.primaryLightColor)
        }


        fun buyButton(button: Button, theme: Theme) {
            buyButton(button, theme.generalTheme)
        }


        fun buyButton(button: Button, theme: GeneralTheme) {
            button.toSelectableButton(
                textColor = theme.primaryTextColor,
                backgroundColor = button.getColor(R.color.colorGreenAccent)
            )
        }


        fun sellButton(button: Button, theme: Theme) {
            sellButton(button, theme.generalTheme)
        }


        fun sellButton(button: Button, theme: GeneralTheme) {
            button.toSelectableButton(
                textColor = theme.primaryTextColor,
                backgroundColor = button.getColor(R.color.colorRedAccent)
            )
        }


    }


    object Orderbook {


        fun contentContainer(view: View, theme: Theme) {
            Main.lightContentContainer(view, theme)
        }


        fun appBarLayout(appBarLayout: AppBarLayout, theme: Theme) {
            Main.lightContentContainer(appBarLayout, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun orderbookDetailsView(orderbookDetailsView: OrderbookDetailsView, theme: Theme) {
            with(orderbookDetailsView) {
                apply(this, theme)

                setHighestBidValueColor(getColor(R.color.colorGreenAccent))
                setLowestAskValueColor(getColor(R.color.colorRedAccent))
            }
        }


        fun orderbookViewTitle(textView: TextView, theme: Theme) {
            Text.secondary(textView, theme)
        }


        fun orderbookView(orderbookView: OrderbookView, theme: Theme) {
            apply(orderbookView, theme)
        }


    }


    object Trade {


        fun contentContainer(view: View, theme: Theme) {
            Main.toolbarBackground(view, theme)
        }


        fun toolbar(toolbar: Toolbar, theme: Theme) {
            Main.toolbar(toolbar, theme)
        }


        fun favoriteButton(imageView: ImageView) {
            imageView.setColor(imageView.getColor(R.color.colorYellowAccent))
        }


        fun unfavoriteButton(imageView: ImageView, theme: Theme) {
            Main.toolbarIcon(imageView, theme)
        }


        fun priceInfoView(priceInfoView: CurrencyMarketPriceInfoView, theme: Theme) {
            priceInfoView(priceInfoView, theme.generalTheme)
        }


        fun priceInfoView(priceInfoView: CurrencyMarketPriceInfoView, theme: GeneralTheme) {
            with(priceInfoView) {
                priceTextColor = theme.primaryTextColor
                fiatPriceTextColor = theme.secondaryTextColor

                dailyPriceChangePositiveColor = getColor(R.color.colorGreenAccent)
                dailyPriceChangeNegativeColor= getColor(R.color.colorRedAccent)
                dailyPriceChangeNeutralColor = getColor(R.color.colorBlueAccent)
            }
        }


        fun userBalanceVerticalMapView(verticalMapView: VerticalMapView, theme: Theme) {
            userBalanceVerticalMapView(verticalMapView, theme.generalTheme)
        }


        fun userBalanceVerticalMapView(verticalMapView: VerticalMapView, theme: GeneralTheme) {
            with(verticalMapView) {
                setTitleColor(theme.secondaryTextColor)
                setSubtitleColor(getColor(R.color.colorYellowAccent))
                setProgressBarColor(theme.accentColor)
                setInfoViewCaptionColor(theme.primaryTextColor)
            }
        }


        fun tabLayout(tabLayout: TabLayout, theme: Theme) {
            TabBar.tabLayout(tabLayout, theme.generalTheme)
        }


        fun orderTypesSwitch(chartTypesSov: SwitchOptionsView, theme: Theme) {
            apply(chartTypesSov, theme)
        }


        fun formView(formView: TradeFormView, theme: Theme) {
            formView(formView, theme.generalTheme)
        }


        fun formView(formView: TradeFormView, theme: GeneralTheme) {
            with(formView) {
                val inputViewHintColor = getColor(R.color.colorInputViewHintColor)

                setLabelTextColor(theme.secondaryTextColor)
                setInputViewLabelTextColor(theme.primaryTextColor)
                setInputViewTextColor(theme.primaryTextColor)
                setInputViewHintTextColor(inputViewHintColor)
                setInputViewCursorColor(theme.primaryTextColor)
                setSeekBarThumbColor(theme.accentColor)
                setSeekBarPrimaryProgressColor(theme.accentColor)
                setSeekBarSecondaryProgressColor(theme.secondaryTextColor)
            }
        }


        fun buyButton(button: Button, theme: Theme) {
            buyButton(button, theme.generalTheme)
        }


        fun buyButton(button: Button, theme: GeneralTheme) {
            button.toSelectableButton(
                textColor = theme.primaryTextColor,
                backgroundColor = button.getColor(R.color.colorGreenAccent)
            )
        }


        fun sellButton(button: Button, theme: Theme) {
            sellButton(button, theme.generalTheme)
        }


        fun sellButton(button: Button, theme: GeneralTheme) {
            button.toSelectableButton(
                textColor = theme.primaryTextColor,
                backgroundColor = button.getColor(R.color.colorRedAccent)
            )
        }


    }


    fun apply(progressBar: ProgressBar, theme: Theme) {
        apply(progressBar, theme.generalTheme)
    }


    fun apply(progressBar: ProgressBar, theme: GeneralTheme) {
        progressBar.setColor(theme.progressBarColor)
    }


    fun apply(swipeRefreshLayout: SwipeRefreshLayout, theme: Theme) {
        apply(swipeRefreshLayout, theme.generalTheme)
    }


    fun apply(swipeRefreshLayout: SwipeRefreshLayout, theme: GeneralTheme) {
        swipeRefreshLayout.setColorSchemeColors(theme.accentColor)
    }


    fun apply(customTabsBuilder: CustomTabsIntent.Builder, theme: Theme) {
        apply(customTabsBuilder, theme.generalTheme)
    }


    fun apply(customTabsBuilder: CustomTabsIntent.Builder, theme: GeneralTheme) {
        customTabsBuilder.setToolbarColor(theme.primaryColor)
        customTabsBuilder.setSecondaryToolbarColor(theme.primaryDarkColor)
    }


    fun apply(switchView: SwitchCompat, theme: Theme) {
        apply(switchView, theme.switchTheme)
    }


    fun apply(switchView: SwitchCompat, theme: SwitchTheme) {
        switchView.setColors(
            theme.pointerDeactivatedColor,
            theme.pointerActivatedColor,
            theme.backgroundDeactivatedColor,
            theme.backgroundActivatedColor
        )
    }


    fun apply(switchOptionsView: SwitchOptionsView, theme: Theme) {
        apply(switchOptionsView, theme.switchOptionsViewTheme)
    }


    fun apply(switchOptionsView: SwitchOptionsView, theme: SwitchOptionsViewTheme) {
        with(switchOptionsView) {
            setOptionsTitleTextColor(theme.titleTextColor)
            setSwitchColor(theme.switchColor)
        }
    }


    fun apply(priceChartView: PriceChartView, theme: Theme) {
        with(priceChartView) {
            val priceChartViewTheme = theme.priceChartViewTheme

            setBackgroundColor(priceChartViewTheme.backgroundColor)
            setProgressBarColor(priceChartViewTheme.progressBarColor)
            setInfoViewCaptionColor(priceChartViewTheme.infoViewColor)
            setChartInfoFieldsDefaultTextColor(priceChartViewTheme.infoFieldsDefaultTextColor)
            setChartAxisGridColor(priceChartViewTheme.axisGridColor)
            setChartHighlighterColor(priceChartViewTheme.highlighterColor)
            setChartPositiveColor(priceChartViewTheme.positiveColor)
            setChartNegativeColor(priceChartViewTheme.negativeColor)
            setChartNeutralColor(priceChartViewTheme.neutralColor)
            setChartVolumeBarColor(priceChartViewTheme.volumeBarsColor)
            setChartCandleStickShadowColor(priceChartViewTheme.candleStickShadowColor)
            setTabBackgroundColor(priceChartViewTheme.tabBackgroundColor)
            setTabTextColors(
                normalColor = priceChartViewTheme.tabNormalTextColor,
                selectedColor = priceChartViewTheme.tabSelectedTextColor
            )

            if(theme.id != com.stocksexchange.android.theming.model.Themes.DEEP_TEAL.id) {
                setInfoViewIconColor(priceChartViewTheme.infoViewColor)
            }
        }
    }


    fun apply(depthChartView: DepthChartView, theme: Theme) {
        with(depthChartView) {
            val depthChartViewTheme = theme.depthChartViewTheme

            setBackgroundColor(depthChartViewTheme.backgroundColor)
            setProgressBarColor(depthChartViewTheme.progressBarColor)
            setInfoViewCaptionColor(depthChartViewTheme.infoViewColor)
            setChartInfoFieldsDefaultTextColor(depthChartViewTheme.infoFieldsDefaultTextColor)
            setChartAxisGridColor(depthChartViewTheme.axisGridColor)
            setChartHighlighterColor(depthChartViewTheme.highlighterColor)
            setChartPositiveColor(depthChartViewTheme.positiveColor)
            setChartNegativeColor(depthChartViewTheme.negativeColor)
            setChartNeutralColor(depthChartViewTheme.neutralColor)
            setTabBackgroundColor(depthChartViewTheme.tabBackgroundColor)
            setTabTextColors(
                normalColor = depthChartViewTheme.tabNormalTextColor,
                selectedColor = depthChartViewTheme.tabSelectedTextColor
            )

            if(theme.id != com.stocksexchange.android.theming.model.Themes.DEEP_TEAL.id) {
                setInfoViewIconColor(depthChartViewTheme.infoViewColor)
            }
        }
    }


    private fun apply(detailsView: BaseDetailsView<*>, theme: Theme) {
        with(detailsView) {
            val generalTheme = theme.generalTheme

            setBackgroundColor(generalTheme.primaryColor)
            setItemTitleColor(generalTheme.secondaryTextColor)
            setItemValueColor(generalTheme.secondaryTextColor)
            setItemSeparatorColor(generalTheme.secondaryTextColor)
            setProgressBarColor(generalTheme.progressBarColor)
            setInfoViewCaptionColor(generalTheme.infoViewColor)

            if(theme.id != com.stocksexchange.android.theming.model.Themes.DEEP_TEAL.id) {
                setInfoViewIconColor(generalTheme.infoViewColor)
            }
        }
    }


    fun apply(orderbookView: OrderbookView, theme: Theme) {
        with(orderbookView) {
            val orderbookViewTheme = theme.orderbookViewTheme

            setBackgroundColor(orderbookViewTheme.backgroundColor)
            setHeaderTitleTextColor(orderbookViewTheme.headerTitleTextColor)
            setHeaderMoreButtonColor(orderbookViewTheme.headerMoreButtonColor)
            setHeaderSeparatorColor(orderbookViewTheme.headerSeparatorColor)
            setBuyPriceColor(orderbookViewTheme.buyPriceColor)
            setBuyPriceHighlightColor(orderbookViewTheme.buyPriceHighlightColor)
            setBuyOrderBackgroundColor(orderbookViewTheme.buyOrderBackgroundColor)
            setBuyHighlightBackgroundColor(orderbookViewTheme.buyOrderHighlightBackgroundColor)
            setSellPriceColor(orderbookViewTheme.sellPriceColor)
            setSellPriceHighlightColor(orderbookViewTheme.sellPriceHighlightColor)
            setSellOrderBackgroundColor(orderbookViewTheme.sellOrderBackgroundColor)
            setSellHighlightBackgroundColor(orderbookViewTheme.sellOrderHighlightBackgroundColor)
            setAmountColor(orderbookViewTheme.amountColor)
            setProgressBarColor(orderbookViewTheme.progressBarColor)
            setInfoViewCaptionColor(orderbookViewTheme.infoViewColor)

            if(theme.id != com.stocksexchange.android.theming.model.Themes.DEEP_TEAL.id) {
                setInfoViewIconColor(orderbookViewTheme.infoViewColor)
            }
        }
    }


    fun apply(tradeHistoryView: TradeHistoryView, theme: Theme) {
        with(tradeHistoryView) {
            val tradeHistoryViewTheme = theme.tradeHistoryViewTheme

            setBackgroundColor(tradeHistoryViewTheme.backgroundColor)
            setHeaderTitleTextColor(tradeHistoryViewTheme.headerTitleTextColor)
            setHeaderSeparatorColor(tradeHistoryViewTheme.headerSeparatorColor)
            setBuyHighlightBackgroundColor(tradeHistoryViewTheme.buyHighlightBackgroundColor)
            setSellHighlightBackgroundColor(tradeHistoryViewTheme.sellHighlightBackgroundColor)
            setBuyPriceHighlightColor(tradeHistoryViewTheme.buyPriceHighlightColor)
            setSellPriceHighlightColor(tradeHistoryViewTheme.sellPriceHighlightColor)
            setBuyPriceColor(tradeHistoryViewTheme.buyPriceColor)
            setSellPriceColor(tradeHistoryViewTheme.sellPriceColor)
            setAmountColor(tradeHistoryViewTheme.amountColor)
            setTradeTimeColor(tradeHistoryViewTheme.tradeTimeColor)
            setProgressBarColor(tradeHistoryViewTheme.progressBarColor)
            setInfoViewCaptionColor(tradeHistoryViewTheme.infoViewColor)
            setCancelButtonTextColor(tradeHistoryViewTheme.amountColor)
            setCancellationProgressBarColor(tradeHistoryViewTheme.progressBarColor)

            if(theme.id != com.stocksexchange.android.theming.model.Themes.DEEP_TEAL.id) {
                setInfoViewIconColor(tradeHistoryViewTheme.infoViewColor)
            }
        }
    }


    fun apply(pinEntryKeypad: PinEntryKeypad, theme: Theme) {
        apply(pinEntryKeypad, theme.pinEntryKeypadTheme)
    }


    fun apply(pinEntryKeypad: PinEntryKeypad, theme: PinEntryKeypadTheme) {
        with(pinEntryKeypad) {
            setDigitButtonTextColor(theme.digitButtonTextColor)
            setDigitButtonColors(
                theme.enabledButtonBackgroundColor,
                theme.disabledButtonBackgroundColor
            )
            setFingerprintButtonColors(
                theme.enabledButtonBackgroundColor,
                theme.disabledButtonBackgroundColor,
                theme.enabledFingerprintButtonForegroundColor,
                theme.disabledActionButtonForegroundColor
            )
            setDeleteButtonColors(
                theme.enabledButtonBackgroundColor,
                theme.disabledButtonBackgroundColor,
                theme.enabledDeleteButtonForegroundColor,
                theme.disabledActionButtonForegroundColor
            )
        }
    }


    fun apply(infoView: InfoView, theme: Theme) {
        apply(infoView, theme.generalTheme)
    }


    fun apply(infoView: InfoView, theme: GeneralTheme) {
        infoView.setCaptionColor(theme.infoViewColor)
    }


    fun apply(popupMenu: PopupMenu, theme: Theme) {
        apply(popupMenu, theme.popupMenuTheme)
    }


    fun apply(popupMenu: PopupMenu, theme: PopupMenuTheme) {
        with(popupMenu) {
            setBackgroundColor(theme.backgroundColor)
            setItemTextColor(theme.titleColor)
            setSeparatorColor(theme.titleColor)
        }
    }


    fun apply(sortPanel: CurrencyMarketsSortPanel, theme: Theme) {
        with(sortPanel) {
            val sortPanelTheme = theme.sortPanelTheme
            val generalTheme = theme.generalTheme

            setBackgroundColor(sortPanelTheme.backgroundColor)
            setSelectedTitleTextColor(sortPanelTheme.selectedTitleColor)
            setUnselectedTitleTextColor(sortPanelTheme.unselectedTitleColor)
            setArrowDrawableColor(generalTheme.primaryTextColor)
            setSeparatorColor(generalTheme.secondaryTextColor)
        }
    }


}