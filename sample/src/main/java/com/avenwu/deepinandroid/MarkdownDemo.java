package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MarkdownDemo extends ActionBarActivity implements ActionBar.TabListener, Content {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown_demo);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public String getContent() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(makeFragmentName(mViewPager.getId(), 0));
        if (fragment instanceof Content) {
            return ((Content) fragment).getContent();
        }
        return "";
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new EditFragment();
                default:
                    return new PreviewFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EditFragment extends Fragment implements Content {
        @InjectView(R.id.edt_input)
        EditText mInputView;
//        String TEMPLATE_DATA = "#Hello\nThis is simple text in format of markdown.\n- li\n- li";
        String TEMPLATE_DATA = "# Mou\n" +
        "\n" +
        "![Mou icon](http://25.io/mou/Mou_128.png)\n" +
        "\n" +
        "## Overview\n" +
        "\n" +
        "**Mou**, the missing Markdown editor for *web developers*.\n" +
        "\n" +
        "### Syntax\n" +
        "\n" +
        "#### Strong and Emphasize \n" +
        "\n" +
        "**strong** or __strong__ ( Cmd + B )\n" +
        "\n" +
        "*emphasize* or _emphasize_ ( Cmd + I )\n" +
        "\n" +
        "**Sometimes I want a lot of text to be bold.\n" +
        "Like, seriously, a _LOT_ of text**\n" +
        "\n" +
        "#### Blockquotes\n" +
        "\n" +
        "> Right angle brackets &gt; are used for block quotes.\n" +
        "\n" +
        "#### Links and Email\n" +
        "\n" +
        "An email <example@example.com> link.\n" +
        "\n" +
        "Simple inline link <http://chenluois.com>, another inline link [Smaller](http://25.io/smaller/), one more inline link with title [Resize](http://resizesafari.com \"a Safari extension\").\n" +
        "\n" +
        "A [reference style][id] link. Input id, then anywhere in the doc, define the link with corresponding id:\n" +
        "\n" +
        "[id]: http://25.io/mou/ \"Markdown editor on Mac OS X\"\n" +
        "\n" +
        "Titles ( or called tool tips ) in the links are optional.\n" +
        "\n" +
        "#### Images\n" +
        "\n" +
        "An inline image ![Smaller icon](http://25.io/smaller/favicon.ico \"Title here\"), title is optional.\n" +
        "\n" +
        "A ![Resize icon][2] reference style image.\n" +
        "\n" +
        "[2]: http://resizesafari.com/favicon.ico \"Title\"\n" +
        "\n" +
        "#### Inline code and Block code\n" +
        "\n" +
        "Inline code are surround by `backtick` key. To create a block code:\n" +
        "\n" +
        "\tIndent each line by at least 1 tab, or 4 spaces.\n" +
        "    var Mou = exactlyTheAppIwant; \n" +
        "\n" +
        "####  Ordered Lists\n" +
        "\n" +
        "Ordered lists are created using \"1.\" + Space:\n" +
        "\n" +
        "1. Ordered list item\n" +
        "2. Ordered list item\n" +
        "3. Ordered list item\n" +
        "\n" +
        "#### Unordered Lists\n" +
        "\n" +
        "Unordered list are created using \"*\" + Space:\n" +
        "\n" +
        "* Unordered list item\n" +
        "* Unordered list item\n" +
        "* Unordered list item \n" +
        "\n" +
        "Or using \"-\" + Space:\n" +
        "\n" +
        "- Unordered list item\n" +
        "- Unordered list item\n" +
        "- Unordered list item\n" +
        "\n" +
        "#### Hard Linebreak\n" +
        "\n" +
        "End a line with two or more spaces will create a hard linebreak, called `<br />` in HTML. ( Control + Return )  \n" +
        "Above line ended with 2 spaces.\n" +
        "\n" +
        "#### Horizontal Rules\n" +
        "\n" +
        "Three or more asterisks or dashes:\n" +
        "\n" +
        "***\n" +
        "\n" +
        "---\n" +
        "\n" +
        "- - - -\n" +
        "\n" +
        "#### Headers\n" +
        "\n" +
        "Setext-style:\n" +
        "\n" +
        "This is H1\n" +
        "==========\n" +
        "\n" +
        "This is H2\n" +
        "----------\n" +
        "\n" +
        "atx-style:\n" +
        "\n" +
        "# This is H1\n" +
        "## This is H2\n" +
        "### This is H3\n" +
        "#### This is H4\n" +
        "##### This is H5\n" +
        "###### This is H6\n" +
        "\n" +
        "\n" +
        "### Extra Syntax\n" +
        "\n" +
        "#### Footnotes\n" +
        "\n" +
        "Footnotes work mostly like reference-style links. A footnote is made of two things: a marker in the text that will become a superscript number; a footnote definition that will be placed in a list of footnotes at the end of the document. A footnote looks like this:\n" +
        "\n" +
        "That's some text with a footnote.[^1]\n" +
        "\n" +
        "[^1]: And that's the footnote.\n" +
        "\n" +
        "\n" +
        "#### Strikethrough\n" +
        "\n" +
        "Wrap with 2 tilde characters:\n" +
        "\n" +
        "~~Strikethrough~~\n" +
        "\n" +
        "\n" +
        "#### Fenced Code Blocks\n" +
        "\n" +
        "Start with a line containing 3 or more backticks, and ends with the first line with the same number of backticks:\n" +
        "\n" +
        "```\n" +
        "Fenced code blocks are like Stardard Markdown’s regular code\n" +
        "blocks, except that they’re not indented and instead rely on\n" +
        "a start and end fence lines to delimit the code block.\n" +
        "```\n" +
        "\n" +
        "\n" +
        "### Shortcuts\n" +
        "\n" +
        "#### View\n" +
        "\n" +
        "* Toggle live preview: Shift + Cmd + I\n" +
        "* Toggle Words Counter: Shift + Cmd + W\n" +
        "* Toggle Transparent: Shift + Cmd + T\n" +
        "* Toggle Floating: Shift + Cmd + F\n" +
        "* Left/Right = 1/1: Cmd + 0\n" +
        "* Left/Right = 3/1: Cmd + +\n" +
        "* Left/Right = 1/3: Cmd + -\n" +
        "* Toggle Writing orientation: Cmd + L\n" +
        "* Toggle fullscreen: Control + Cmd + F\n" +
        "\n" +
        "#### Actions\n" +
        "\n" +
        "* Copy HTML: Option + Cmd + C\n" +
        "* Strong: Select text, Cmd + B\n" +
        "* Emphasize: Select text, Cmd + I\n" +
        "* Inline Code: Select text, Cmd + K\n" +
        "* Strikethrough: Select text, Cmd + U\n" +
        "* Link: Select text, Control + Shift + L\n" +
        "* Image: Select text, Control + Shift + I\n" +
        "* Select Word: Control + Option + W\n" +
        "* Select Line: Shift + Cmd + L\n" +
        "* Select All: Cmd + A\n" +
        "* Deselect All: Cmd + D\n" +
        "* Convert to Uppercase: Select text, Control + U\n" +
        "* Convert to Lowercase: Select text, Control + Shift + U\n" +
        "* Convert to Titlecase: Select text, Control + Option + U\n" +
        "* Convert to List: Select lines, Control + L\n" +
        "* Convert to Blockquote: Select lines, Control + Q\n" +
        "* Convert to H1: Cmd + 1\n" +
        "* Convert to H2: Cmd + 2\n" +
        "* Convert to H3: Cmd + 3\n" +
        "* Convert to H4: Cmd + 4\n" +
        "* Convert to H5: Cmd + 5\n" +
        "* Convert to H6: Cmd + 6\n" +
        "* Convert Spaces to Tabs: Control + [\n" +
        "* Convert Tabs to Spaces: Control + ]\n" +
        "* Insert Current Date: Control + Shift + 1\n" +
        "* Insert Current Time: Control + Shift + 2\n" +
        "* Insert entity <: Control + Shift + ,\n" +
        "* Insert entity >: Control + Shift + .\n" +
        "* Insert entity &: Control + Shift + 7\n" +
        "* Insert entity Space: Control + Shift + Space\n" +
        "* Insert Scriptogr.am Header: Control + Shift + G\n" +
        "* Shift Line Left: Select lines, Cmd + [\n" +
        "* Shift Line Right: Select lines, Cmd + ]\n" +
        "* New Line: Cmd + Return\n" +
        "* Comment: Cmd + /\n" +
        "* Hard Linebreak: Control + Return\n" +
        "\n" +
        "#### Edit\n" +
        "\n" +
        "* Auto complete current word: Esc\n" +
        "* Find: Cmd + F\n" +
        "* Close find bar: Esc\n" +
        "\n" +
        "#### Post\n" +
        "\n" +
        "* Post on Scriptogr.am: Control + Shift + S\n" +
        "* Post on Tumblr: Control + Shift + T\n" +
        "\n" +
        "#### Export\n" +
        "\n" +
        "* Export HTML: Option + Cmd + E\n" +
        "* Export PDF:  Option + Cmd + P\n" +
        "\n" +
        "\n" +
        "### And more?\n" +
        "\n" +
        "Don't forget to check Preferences, lots of useful options are there.\n" +
        "\n" +
        "Follow [@Mou](https://twitter.com/mou) on Twitter for the latest news.\n" +
        "\n" +
        "For feedback, use the menu `Help` - `Send Feedback`";

        public EditFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.markdown_edit, container, false);
            ButterKnife.inject(this, rootView);
            mInputView.setText(TEMPLATE_DATA);
            return rootView;
        }

        @Override
        public String getContent() {
            return mInputView.getText().toString();
        }
    }

    public static class PreviewFragment extends Fragment {
        private WebView mWebView;
        private boolean mIsWebViewAvailable;
        private Markdown4jProcessor mProcessor;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (mWebView != null) {
                mWebView.destroy();
            }
            mWebView = new WebView(getActivity());
            mIsWebViewAvailable = true;
            mProcessor = new Markdown4jProcessor();
            return mWebView;
        }

        @Override
        public void onPause() {
            super.onPause();
            mWebView.onPause();
        }

        @Override
        public void onResume() {
            mWebView.onResume();
            super.onResume();
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                String data = ((Content) getActivity()).getContent();
                mWebView.loadData(renderMarkdownString(data), "text/html", "UTF-8");
            }
        }

        String mTemplate = "<link rel=\"stylesheet\" href=\"file:///android_asset/github-markdown.css\"/><div class=\"markdown-body\">%s</div>";

        String renderMarkdownString(String raw) {
            try {
                return String.format(mTemplate, mProcessor.process(raw));
            } catch (IOException e) {
                e.printStackTrace();
                return String.format(mTemplate, raw);
            }
        }

        @Override
        public void onDestroyView() {
            mIsWebViewAvailable = false;
            super.onDestroyView();
        }

        @Override
        public void onDestroy() {
            if (mWebView != null) {
                mWebView.destroy();
                mWebView = null;
            }
            super.onDestroy();
        }

    }
}
