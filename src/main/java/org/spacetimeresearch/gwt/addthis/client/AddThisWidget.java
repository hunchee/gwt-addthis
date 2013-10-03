/*
 *    Copyright 2009 Space-Time Research Pty Ltd
 *    http://www.spacetimereseach.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package org.spacetimeresearch.gwt.addthis.client;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;


/**
 * 
 * AddThisWidget.
 * A GWT Widget that wraps AddThis javaScript to create a GWT button version.
 * 
 * <p>
 * Created: Dec 18, 2009
 * License: Apache License, Version 2.0
 * 
 * @author Ben Hudson, Brendan Sebire & Karl Ots
 * Space-Time Research Pty Ltd
 * http://www.spacetimeresearch.com
 * @since
 */
public class AddThisWidget extends Widget {
  /** Popup default time out in Milliseconds **/
  private static final int POPUP_SCHEDULE_DEFAULT = 500;

  /** Stops reloading JavaScript to the DOM on pubId basis */
  private static Set<String> pubIdLoadedScript;
  
  /** on mouse out event name */
  private static final String ONMOUSEOUT = "onmouseout";
  
  /** Anchor elements class name */
  private static final String CLASS_NAME = "str_addthis_button";

  /** JavaScripts href less pubic id */
  private static final String HREF = "http://www.addthis.com/bookmark.php?v=250&pub=";

  /** the button image tag */
  private static final String BUTTON_IMAGE_HTML = "<img src='http://s7.addthis.com/static/btn/v2/lg-share-en.gif' width='%1$' height='%2$' alt='Bookmark and Share' style='border:0'/>";

  /** The auto element ID prefix. */
  private static String autoElementIdPrefix = "addthis-button-auto";

  /** The next auto element ID number. */
  private static int nextAutoElementIdNumber;

  /** public id used by addthis server side to identify the client (used for analytics) */
  private String pubId;
  
  /** message used in addthis action (e.g. Tweet message) */
  private String message = "SuperVIEW By Space-Time Research Pty Ltd";
  
  /** anchor element which this widget wraps */
  private AnchorElement anchorElement;
  
  /** button img width */
  private int width = 125;

  /** button img height */
  private int height = 16;

  /** popup on mouse over schedule time */
  private int popupSchedule;
  
  /** the menu popup on mouse over timer */
  private Timer popupTimer;

  /**
   * Construct a new AddThisWidget.
   *
   * @param pubId addthis identification.
   * @param message addthis message.
   * @param popupSchedule delay time in Milliseconds to render the popup.
   */
  public AddThisWidget(String pubId, String message, int popupSchedule) {    
    super();
    if (pubIdLoadedScript == null){
      pubIdLoadedScript = new HashSet<String>();
    }
    Document doc = Document.get();
    if (!pubIdLoadedScript.contains(pubId)){
      ScriptElement script = doc.createScriptElement();
      script.setSrc("http://s7.addthis.com/js/250/addthis_widget.js#pub="+pubId);
      script.setType("text/javascript");
      script.setLang("javascript");
      doc.getBody().appendChild(script);
      pubIdLoadedScript.add(pubId);
    }
        
    this.pubId = pubId;
    assert getPubId() != null : "pubID not set to a non null value!";

    if (message != null){
      this.message = message;
    }
    
    setPopupSchedule(popupSchedule);
    this.anchorElement = doc.createAnchorElement();
    this.setElement(anchorElement);
  }
  
  /**
   * Construct a new AddThisWidget.
   *
   * @param pubId addthis identification.
   * @param message addthis default message.
   */
  public AddThisWidget(String pubId, String message) {
    this(pubId, message, POPUP_SCHEDULE_DEFAULT);
  }
  
  /**
   * Construct a new AddThisWidget.
   *
   * @param pubId addthis identification.
   */
  public AddThisWidget(String pubId) {
    this(pubId, null);
  }
  
  /**
   * Retrieve the auto element ID prefix.
   *
   * @return The auto element ID prefix.
   * @see #setAutoElementIdPrefix(String)
   */
  public static String getAutoElementIdPrefix() {
    return AddThisWidget.autoElementIdPrefix;
  }
  
  /**
   * Set the auto element ID prefix.
   *
   * @param autoElementIdPrefix The auto element ID prefix.
   * @see #getAutoElementIdPrefix()
   */
  public static void setAutoElementIdPrefix(String autoElementIdPrefix) {
    assert autoElementIdPrefix != null && !autoElementIdPrefix.isEmpty() : "Auto element ID prefix cannot be null or empty";
    
    AddThisWidget.autoElementIdPrefix = autoElementIdPrefix;
  }

  /**
   * Gets the public id
   * 
   * @return The addThis publication id
   */
  public String getPubId() {
    return this.pubId;
  }
  
  /**
   * Sets the public id
   * 
   * @param pubId The addThis publication id
   */
  public void setPubId(String pubId) {
    this.pubId = pubId;
  }

  /**
   * Gets button width
   * 
   * @return The width of the button
   */
  public int getButtonWidth() {
    return this.width;
  }
  
  /**
   * Sets button width
   *
   * @param width The width of the button
   */
  public void setButtonWidth(int width) {
    this.width = width;
  }
  
  /**
   * Gets button height
   *
   * @return The height of the button
   */
  public int getButtonHeight() {
    return this.height;
  }

  /**
   * Sets button height
   *
   * @param height The height of the button
   */
  public void setButtonHeight(int height) {
    this.height = height;
  }

  /**
   * Get the menu popup delay
   * 
   * @return The delay in ms before the addThis menu appears when the mouse hovers over it
   */  
  public int getPopupSchedule() {
    return this.popupSchedule;
  }

  /**
   * Sets the menu popup delay
   * 
   * @param popupSchedule The delay in ms before the addThis menu appears when the mouse hovers over it
   */  
  public void setPopupSchedule(int popupSchedule) {
    this.popupSchedule = popupSchedule;
  }

  /**
   * Get the menu popup timer
   * 
   * @return The popup timer 
   */  
  public Timer getPopupTimer() {
    return this.popupTimer;
  }

  /**
   * Sets the menu popup timer
   * 
   * @param popupTimer The popup timer
   */  
  public void setPopupTimer(Timer popupTimer) {
    this.popupTimer = popupTimer;
  }

  /**
   * Creates the AddThisWidget's id and sets its state.
   */
  @Override
  protected void onAttach() {
    assert this.anchorElement != null : "Anchor element not set";
    
    Element element = this.getElement();
    
    String elementId = element.getId();
    if (elementId == null || elementId.isEmpty()) {
      element.setId(getNextAutoElementId());
    }
    
    createAddThis();
    
    super.onAttach();
  }

  /**
   * Retrieve the next auto element ID.
   *
   * @return The next auto element ID.
   */
  private synchronized static String getNextAutoElementId() {
    return autoElementIdPrefix + "-" + (++nextAutoElementIdNumber);
  }

  /**
   * Sets the state of the anchorElement and handles the mouse over events.
   */
  private void createAddThis() {
    this.anchorElement.addClassName(CLASS_NAME);
    this.anchorElement.setAttribute(ONMOUSEOUT, "addthis_close();");
    this.anchorElement.setHref(HREF+getPubId());
    this.anchorElement.setInnerHTML(createButtonImageHtml(width, height));
    addMouseOverHandler();
    addMouseOutHandler();
    addClickHandler();
  }

  /**
   * Adds a mouse over handler to set the popup Schedule delay timer.
   */
  private void addMouseOutHandler() {
    this.addDomHandler(new MouseOutHandler() {      
      @Override
      public void onMouseOut(MouseOutEvent event) {
        getPopupTimer().cancel();
      }
    }, MouseOutEvent.getType());
  }

  /**
   * Cancels the <code>popupTimer</code>
   */
  private void addMouseOverHandler() {
    this.addDomHandler(new MouseOverHandler() {     
      @Override
      public void onMouseOver(MouseOverEvent event) {
        setPopupTimer( new Timer(){
          @Override
          public void run() {
            addThisMouseOver(anchorElement, Window.Location.getHref(), message);
          }
        });
        getPopupTimer().schedule(getPopupSchedule());
      }
    }, MouseOverEvent.getType());
  }
  
  /**
   * Shows the "more" popup menu when the addThis button is directly clicked
   */
  private void addClickHandler() {
    this.addDomHandler(new ClickHandler() {     
      @Override
      public void onClick(ClickEvent event) {
        addThisClick(anchorElement, Window.Location.getHref(), message);
        event.preventDefault();
      }
    }, ClickEvent.getType());
  }

  /**
   * Creates the img tag for the button and sets its width & height.
   *
   * @param width
   * @param height
   * @return img tag.
   */
  private String createButtonImageHtml(int width, int height) {
    return BUTTON_IMAGE_HTML.replace("%1$", String.valueOf(width))
                            .replace("%2$", String.valueOf(height));
  }
  
  /**
   * Changes the URL to be used by the add this button.
   *
   * @param addThisElement
   * @param href
   * @param message
   */
  private native void addThisMouseOver(AnchorElement addThisElement, String href, String message) /*-{
    $wnd.addthis_open(addThisElement, "", href, message);
  }-*/;

  /**
   * Changes the URL to be used by the add this button.
   *
   * @param addThisElement
   * @param href
   * @param message
   */
  private native void addThisClick(AnchorElement addThisElement, String href, String message) /*-{
    $wnd.addthis_open(addThisElement, "more", href, message);
  }-*/;
}