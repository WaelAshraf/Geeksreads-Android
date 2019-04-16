package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorActivityTest {
    @Rule
    public ActivityTestRule<AuthorActivity> menuActivityTestRule =
            new ActivityTestRule<>(AuthorActivity.class, true, true);


    @Test
    void Test()
    {
        String Disc="Jane Austen was an English novelist whose works of romantic fiction, set among the landed gentry, earned her a place as one of the most widely read writers in English literature, her realism and biting social commentary cementing her historical importance among scholars and critics.\n" +
                "\n" +
                "Austen lived her entire life as part of a close-knit family located on the lower fringes of the English landed gentry. She was educated primarily by her father and older brothers as well as through her own reading. The steadfast support of her family was critical to her development as a professional writer. Her artistic apprenticeship lasted from her teenage years until she was about 35 years old. During this period, she experimented with various literary forms, including the epistolary novel which she tried then abandoned, and wrote and extensively revised three major novels and began a fourth. From 1811 until 1816, with the release of Sense and Sensibility (1811), Pride and Prejudice (1813), Mansfield Park (1814) and Emma (1815), she achieved success as a published writer. She wrote two additional novels, Northanger Abbey and Persuasion, both published posthumously in 1818, and began a third, which was eventually titled Sanditon, but died before completing it.\n" +
                "\n" +
                "Austen's works critique the novels of sensibility of the second half of the 18th century and are part of the transition to 19th-century realism. Her plots, though fundamentally comic, highlight the dependence of women on marriage to secure social standing and economic security. Her work brought her little personal fame and only a few positive reviews during her lifetime, but the publication in 1869 of her nephew's A Memoir of Jane Austen introduced her to a wider public, and by the 1940s she had become widely accepted in academia as a great English writer. The second half of the 20th century saw a proliferation of Austen scholarship and the emergence of a Janeite fan culture.";
        assertEquals("Jane Austen", AuthorActivity.sForTestAuthorName);

        /* Testing getting Title of the Book right */
        assertEquals("Author of 274 books.", AuthorActivity.sForTestNumOfBooks);

        /* Testing getting Rating of the Book right */
        assertEquals("4.6", AuthorActivity.sForTestAuthorRate);

        /* Testing getting Date of the Book right */
        assertEquals("120,000 ratings.", AuthorActivity.sForTestAuthorNumOfRates);

        assertEquals("72,562 reviews.",AuthorActivity.sForTestAuthorNumOfReviews);

        /* Testing The finishing of all Async Tasks */
        assertEquals("Following", AuthorActivity.sFortTestFollowStatus);
        assertEquals(Disc,AuthorActivity.sForTestAuthorDescription);
        assertEquals("http://geeksreads.000webhostapp.com/Fatema/janeausten.jpg",AuthorActivity.sForTestAuthorPicURL);
    }
}