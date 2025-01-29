stateDiagram-v2
    [*] --> Authentication: User Login/Signup
    
    state Authentication {
        [*] --> OAuth
        [*] --> EmailRegistration
        OAuth --> ProfileCreation
        EmailRegistration --> ProfileCreation
    }
    
    state CoreFeatures {
        HomeFeed
        ProjectShowcase
        CodeSnippets
        TechForum
        CollaborationTools
    }
    
    Authentication --> CoreFeatures: Successful Login
    
    state Monetization {
        PremiumFeatures
        JobMatching
        SponsoredContent
        ConsultationBooking
    }
    
    CoreFeatures --> Monetization: Advanced Interactions
