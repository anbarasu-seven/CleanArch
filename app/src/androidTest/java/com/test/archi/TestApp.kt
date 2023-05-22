package com.test.archi

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.android.example.github.util.GithubTestRunner].
 */
class TestApp : Application()
