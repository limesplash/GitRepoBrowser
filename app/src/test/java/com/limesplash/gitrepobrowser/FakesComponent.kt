package com.limesplash.gitrepobrowser

import dagger.Component

@Component(modules = [FakesModule::class])
interface FakesComponent: GitReposComponent {
}